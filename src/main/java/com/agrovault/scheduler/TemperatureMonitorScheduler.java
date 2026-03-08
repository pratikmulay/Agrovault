package com.agrovault.scheduler;

import com.agrovault.entity.Storage;
import com.agrovault.entity.TemperatureLog;
import com.agrovault.event.TemperatureAlertEvent;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.TemperatureLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitorScheduler {

    private final StorageRepository storageRepository;
    private final TemperatureLogRepository temperatureLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 60000)
    public void checkTemperatureLogs() {
        List<Storage> storages = storageRepository.findAll();

        for (Storage storage : storages) {
            Optional<TemperatureLog> latestLog = temperatureLogRepository
                    .findTopByStorageOrderByRecordedAtDesc(storage);

            if (latestLog.isPresent()) {
                double temp = latestLog.get().getTemperature();
                Double min = storage.getTemperatureMin();
                Double max = storage.getTemperatureMax();

                boolean breach = (min != null && temp < min) || (max != null && temp > max);

                if (breach) {
                    String alertMessage = String.format(
                            "Temperature breach at %s! Current=%.1f, Allowed=[%s, %s]",
                            storage.getName(), temp,
                            min != null ? String.format("%.1f", min) : "N/A",
                            max != null ? String.format("%.1f", max) : "N/A");

                    log.warn("[SCHEDULER] {}", alertMessage);
                    eventPublisher.publishEvent(new TemperatureAlertEvent(this, storage, temp, alertMessage));
                } else {
                    log.info("[SCHEDULER] Storage {} - Temperature OK: {}", storage.getName(), temp);
                }
            } else {
                log.info("[SCHEDULER] Storage {} - No temperature logs found", storage.getName());
            }
        }
    }
}
