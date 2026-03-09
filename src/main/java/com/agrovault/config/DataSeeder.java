package com.agrovault.config;

import com.agrovault.entity.Booking;
import com.agrovault.entity.BookingStatus;
import com.agrovault.entity.City;
import com.agrovault.entity.Role;
import com.agrovault.entity.Storage;
import com.agrovault.entity.TemperatureLog;
import com.agrovault.entity.User;
import com.agrovault.repository.BookingRepository;
import com.agrovault.repository.CityRepository;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.TemperatureLogRepository;
import com.agrovault.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final StorageRepository storageRepository;
    private final BookingRepository bookingRepository;
    private final TemperatureLogRepository temperatureLogRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CityRepository cityRepository,
                      StorageRepository storageRepository,
                      BookingRepository bookingRepository,
                      TemperatureLogRepository temperatureLogRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.storageRepository = storageRepository;
        this.bookingRepository = bookingRepository;
        this.temperatureLogRepository = temperatureLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        // ── STEP 1: Seed Cities ──
        Map<String, City> cities = new HashMap<>();
        cities.put("Nashik", cityRepository.save(City.builder().name("Nashik").latitude(20.0059).longitude(73.7794).build()));
        cities.put("Pune", cityRepository.save(City.builder().name("Pune").latitude(18.5204).longitude(73.8567).build()));
        cities.put("Chh. Sambhajinagar", cityRepository.save(City.builder().name("Chh. Sambhajinagar").latitude(19.8762).longitude(75.3433).build()));
        cities.put("Nagpur", cityRepository.save(City.builder().name("Nagpur").latitude(21.1458).longitude(79.0882).build()));
        cities.put("Ahmednagar", cityRepository.save(City.builder().name("Ahmednagar").latitude(19.0948).longitude(74.7480).build()));
        cities.put("Solapur", cityRepository.save(City.builder().name("Solapur").latitude(17.6868).longitude(75.9100).build()));
        cities.put("Latur", cityRepository.save(City.builder().name("Latur").latitude(18.4088).longitude(76.5604).build()));
        cities.put("Satara", cityRepository.save(City.builder().name("Satara").latitude(17.6805).longitude(74.0183).build()));
        cities.put("Beed", cityRepository.save(City.builder().name("Beed").latitude(18.9890).longitude(75.7601).build()));
        cities.put("Sangamner", cityRepository.save(City.builder().name("Sangamner").latitude(19.5707).longitude(74.2095).build()));
        System.out.println("[AGROVAULT SEEDER] ✓ 10 cities inserted");

        // ── STEP 2: Seed Users ──
        Map<String, User> users = new HashMap<>();

        // Admin
        users.put("admin", userRepository.save(User.builder()
                .name("Admin AgroVault").email("admin@agrovault.com")
                .password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build()));

        // Storage Owners
        users.put("owner1", userRepository.save(User.builder()
                .name("Rajan Patil").email("owner1@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));
        users.put("owner2", userRepository.save(User.builder()
                .name("Suresh Deshmukh").email("owner2@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));
        users.put("owner3", userRepository.save(User.builder()
                .name("Anita Shinde").email("owner3@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));
        users.put("owner4", userRepository.save(User.builder()
                .name("Prakash Jadhav").email("owner4@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));
        users.put("owner5", userRepository.save(User.builder()
                .name("Meena Kulkarni").email("owner5@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));
        users.put("owner6", userRepository.save(User.builder()
                .name("Vijay Mane").email("owner6@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));
        users.put("owner7", userRepository.save(User.builder()
                .name("Sanjay Gaikwad").email("owner7@agrovault.com")
                .password(passwordEncoder.encode("owner123")).role(Role.STORAGE_OWNER).build()));

        // Farmers
        users.put("farmer1", userRepository.save(User.builder()
                .name("Balaji Thorat").email("farmer1@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer2", userRepository.save(User.builder()
                .name("Priya Nimbalkar").email("farmer2@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer3", userRepository.save(User.builder()
                .name("Santosh Salve").email("farmer3@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer4", userRepository.save(User.builder()
                .name("Rekha Pawar").email("farmer4@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer5", userRepository.save(User.builder()
                .name("Anil Bhosale").email("farmer5@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer6", userRepository.save(User.builder()
                .name("Sunita Waghmare").email("farmer6@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer7", userRepository.save(User.builder()
                .name("Dnyaneshwar Gavhane").email("farmer7@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer8", userRepository.save(User.builder()
                .name("Kavita Londhe").email("farmer8@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer9", userRepository.save(User.builder()
                .name("Ramesh Dhole").email("farmer9@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer10", userRepository.save(User.builder()
                .name("Shanta Bansode").email("farmer10@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer11", userRepository.save(User.builder()
                .name("Ganesh Kale").email("farmer11@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        users.put("farmer12", userRepository.save(User.builder()
                .name("Lata Kharat").email("farmer12@agrovault.com")
                .password(passwordEncoder.encode("farmer123")).role(Role.FARMER).build()));
        System.out.println("[AGROVAULT SEEDER] ✓ 20 users inserted (1 admin, 7 owners, 12 farmers)");

        // ── STEP 3: Seed Storages ──
        Map<String, Storage> storages = new HashMap<>();

        storages.put("Nashik GrapeFrost Vault", storageRepository.save(buildStorage(
                "Nashik GrapeFrost Vault", users.get("owner1"), cities.get("Nashik"),
                500.0, 400.0, 2.0, 8.0, 0.02, 0.01)));
        storages.put("Nashik WineCool Storage", storageRepository.save(buildStorage(
                "Nashik WineCool Storage", users.get("owner2"), cities.get("Nashik"),
                300.0, 250.0, -5.0, 5.0, -0.03, 0.02)));
        storages.put("Pune AgriChill Hub", storageRepository.save(buildStorage(
                "Pune AgriChill Hub", users.get("owner3"), cities.get("Pune"),
                450.0, 380.0, 2.0, 8.0, 0.01, -0.02)));
        storages.put("Pune FreshKeep Depot", storageRepository.save(buildStorage(
                "Pune FreshKeep Depot", users.get("owner4"), cities.get("Pune"),
                350.0, 280.0, 8.0, 15.0, -0.02, 0.03)));
        storages.put("Sambhajinagar OniCold Store", storageRepository.save(buildStorage(
                "Sambhajinagar OniCold Store", users.get("owner5"), cities.get("Chh. Sambhajinagar"),
                400.0, 320.0, 8.0, 15.0, 0.03, -0.01)));
        storages.put("Sambhajinagar PomeFrost", storageRepository.save(buildStorage(
                "Sambhajinagar PomeFrost", users.get("owner6"), cities.get("Chh. Sambhajinagar"),
                250.0, 200.0, 2.0, 8.0, -0.01, 0.04)));
        storages.put("Nagpur OrangeChill Depot", storageRepository.save(buildStorage(
                "Nagpur OrangeChill Depot", users.get("owner7"), cities.get("Nagpur"),
                500.0, 420.0, 2.0, 8.0, 0.04, -0.03)));
        storages.put("Nagpur MangoFreeze Hub", storageRepository.save(buildStorage(
                "Nagpur MangoFreeze Hub", users.get("owner1"), cities.get("Nagpur"),
                350.0, 300.0, -5.0, 5.0, -0.02, 0.01)));
        storages.put("Ahmednagar TomatoCool Vault", storageRepository.save(buildStorage(
                "Ahmednagar TomatoCool Vault", users.get("owner2"), cities.get("Ahmednagar"),
                300.0, 240.0, 8.0, 15.0, 0.01, -0.04)));
        storages.put("Ahmednagar OnionKeep Store", storageRepository.save(buildStorage(
                "Ahmednagar OnionKeep Store", users.get("owner3"), cities.get("Ahmednagar"),
                450.0, 400.0, 8.0, 15.0, -0.04, 0.02)));
        storages.put("Solapur BananaChill Centre", storageRepository.save(buildStorage(
                "Solapur BananaChill Centre", users.get("owner4"), cities.get("Solapur"),
                300.0, 250.0, 2.0, 8.0, 0.03, 0.03)));
        storages.put("Solapur AgriFreeze Depot", storageRepository.save(buildStorage(
                "Solapur AgriFreeze Depot", users.get("owner5"), cities.get("Solapur"),
                200.0, 160.0, -5.0, 5.0, -0.01, -0.02)));
        storages.put("Latur PomeGrape Vault", storageRepository.save(buildStorage(
                "Latur PomeGrape Vault", users.get("owner6"), cities.get("Latur"),
                400.0, 320.0, 2.0, 8.0, 0.02, -0.03)));
        storages.put("Latur CoolRoots Storage", storageRepository.save(buildStorage(
                "Latur CoolRoots Storage", users.get("owner7"), cities.get("Latur"),
                250.0, 200.0, 8.0, 15.0, -0.03, 0.04)));
        storages.put("Satara MangoKeep Hub", storageRepository.save(buildStorage(
                "Satara MangoKeep Hub", users.get("owner1"), cities.get("Satara"),
                350.0, 300.0, 2.0, 8.0, 0.04, 0.01)));
        storages.put("Satara FarmFrost Centre", storageRepository.save(buildStorage(
                "Satara FarmFrost Centre", users.get("owner2"), cities.get("Satara"),
                200.0, 180.0, -5.0, 5.0, -0.04, -0.01)));
        storages.put("Beed GrapeVault Cold", storageRepository.save(buildStorage(
                "Beed GrapeVault Cold", users.get("owner3"), cities.get("Beed"),
                300.0, 250.0, 2.0, 8.0, 0.01, 0.03)));
        storages.put("Beed OnionChill Depot", storageRepository.save(buildStorage(
                "Beed OnionChill Depot", users.get("owner4"), cities.get("Beed"),
                400.0, 340.0, 8.0, 15.0, -0.02, -0.04)));
        storages.put("Sangamner TomatoFreeze", storageRepository.save(buildStorage(
                "Sangamner TomatoFreeze", users.get("owner5"), cities.get("Sangamner"),
                200.0, 160.0, -5.0, 5.0, 0.03, -0.02)));
        storages.put("Sangamner AgroCool Hub", storageRepository.save(buildStorage(
                "Sangamner AgroCool Hub", users.get("owner6"), cities.get("Sangamner"),
                350.0, 280.0, 2.0, 8.0, -0.01, 0.03)));
        System.out.println("[AGROVAULT SEEDER] ✓ 20 cold storages inserted");

        // ── STEP 4: Seed Bookings ──
        // For CONFIRMED/COMPLETED bookings, deduct quantity from availableCapacity
        seedBooking(users.get("farmer1"), storages.get("Nashik GrapeFrost Vault"), "Grapes",
                50.0, LocalDate.of(2025, 1, 10), LocalDate.of(2025, 3, 10), BookingStatus.COMPLETED);
        seedBooking(users.get("farmer2"), storages.get("Pune AgriChill Hub"), "Tomatoes",
                40.0, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 4, 1), BookingStatus.COMPLETED);
        seedBooking(users.get("farmer3"), storages.get("Nagpur OrangeChill Depot"), "Oranges",
                60.0, LocalDate.of(2025, 3, 15), LocalDate.of(2025, 6, 15), BookingStatus.CONFIRMED);
        seedBooking(users.get("farmer4"), storages.get("Sambhajinagar OniCold Store"), "Onions",
                80.0, LocalDate.of(2025, 4, 1), LocalDate.of(2025, 7, 1), BookingStatus.CONFIRMED);
        seedBooking(users.get("farmer5"), storages.get("Nashik WineCool Storage"), "Grapes",
                30.0, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 8, 1), BookingStatus.CONFIRMED);
        seedBooking(users.get("farmer6"), storages.get("Nagpur MangoFreeze Hub"), "Mangoes",
                50.0, LocalDate.of(2025, 5, 10), LocalDate.of(2025, 7, 10), BookingStatus.CONFIRMED);
        seedBooking(users.get("farmer7"), storages.get("Ahmednagar TomatoCool Vault"), "Tomatoes",
                40.0, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 9, 1), BookingStatus.PENDING);
        seedBooking(users.get("farmer8"), storages.get("Latur PomeGrape Vault"), "Pomegranate",
                30.0, LocalDate.of(2025, 6, 15), LocalDate.of(2025, 9, 15), BookingStatus.PENDING);
        seedBooking(users.get("farmer9"), storages.get("Solapur BananaChill Centre"), "Bananas",
                50.0, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 10, 1), BookingStatus.PENDING);
        seedBooking(users.get("farmer10"), storages.get("Satara MangoKeep Hub"), "Mangoes",
                60.0, LocalDate.of(2025, 7, 10), LocalDate.of(2025, 10, 10), BookingStatus.PENDING);
        seedBooking(users.get("farmer11"), storages.get("Beed GrapeVault Cold"), "Grapes",
                40.0, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 11, 1), BookingStatus.CANCELLED);
        seedBooking(users.get("farmer12"), storages.get("Sangamner TomatoFreeze"), "Tomatoes",
                30.0, LocalDate.of(2025, 8, 15), LocalDate.of(2025, 11, 15), BookingStatus.CANCELLED);
        System.out.println("[AGROVAULT SEEDER] ✓ 12 bookings inserted");

        // ── STEP 5: Seed Temperature Logs ──
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        LocalDateTime thirtyMinAgo = LocalDateTime.now().minusMinutes(30);

        for (Map.Entry<String, Storage> entry : storages.entrySet()) {
            Storage s = entry.getValue();
            String storageName = entry.getKey();
            double normalTemp = (s.getTemperatureMin() + s.getTemperatureMax()) / 2.0;

            // Log 1: normal reading for every storage
            temperatureLogRepository.save(TemperatureLog.builder()
                    .storage(s).temperature(normalTemp).humidity(77.0).recordedAt(twoHoursAgo).build());

            // Log 2: breach for selected storages, otherwise another normal reading
            double log2Temp;
            double log2Humidity;
            LocalDateTime log2Time;

            switch (storageName) {
                case "Nashik WineCool Storage" -> {
                    log2Temp = 9.5;
                    log2Humidity = 80.0;
                    log2Time = thirtyMinAgo;
                }
                case "Nagpur MangoFreeze Hub" -> {
                    log2Temp = 8.2;
                    log2Humidity = 82.0;
                    log2Time = thirtyMinAgo;
                }
                case "Solapur AgriFreeze Depot" -> {
                    log2Temp = 7.1;
                    log2Humidity = 75.0;
                    log2Time = thirtyMinAgo;
                }
                case "Sangamner TomatoFreeze" -> {
                    log2Temp = 8.8;
                    log2Humidity = 78.0;
                    log2Time = thirtyMinAgo;
                }
                case "Satara FarmFrost Centre" -> {
                    log2Temp = 6.5;
                    log2Humidity = 73.0;
                    log2Time = thirtyMinAgo;
                }
                default -> {
                    log2Temp = normalTemp;
                    log2Humidity = 77.0;
                    log2Time = twoHoursAgo;
                }
            }

            temperatureLogRepository.save(TemperatureLog.builder()
                    .storage(s).temperature(log2Temp).humidity(log2Humidity).recordedAt(log2Time).build());
        }
        System.out.println("[AGROVAULT SEEDER] ✓ 40 temperature logs inserted (5 breach logs for scheduler demo)");

        System.out.println("[AGROVAULT SEEDER] Database seeded successfully. Ready for demo.");
    }

    private Storage buildStorage(String name, User owner, City city,
                                 double totalCapacity, double availableCapacity,
                                 double tempMin, double tempMax,
                                 double latOffset, double lonOffset) {
        return Storage.builder()
                .name(name)
                .owner(owner)
                .city(city)
                .latitude(city.getLatitude() + latOffset)
                .longitude(city.getLongitude() + lonOffset)
                .totalCapacity(totalCapacity)
                .availableCapacity(availableCapacity)
                .temperatureMin(tempMin)
                .temperatureMax(tempMax)
                .build();
    }

    private void seedBooking(User farmer, Storage storage, String produceType,
                             double quantity, LocalDate start, LocalDate end, BookingStatus status) {
        Booking booking = Booking.builder()
                .farmer(farmer)
                .storage(storage)
                .produceType(produceType)
                .quantity(quantity)
                .startDate(start)
                .endDate(end)
                .status(status)
                .build();
        bookingRepository.save(booking);

        // Deduct capacity for CONFIRMED and COMPLETED bookings
        if (status == BookingStatus.CONFIRMED || status == BookingStatus.COMPLETED) {
            storage.setAvailableCapacity(storage.getAvailableCapacity() - quantity);
            storageRepository.save(storage);
        }
    }
}
