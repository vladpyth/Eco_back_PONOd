package com.example.eco_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.eco_service.entities.*;
import com.example.eco_service.repositories.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    // Репозитории для новых сущностей (все с префиксом Interf)
    private final InterfRegion regionRepository;
    private final InterfDistrict districtRepository;
    private final InterfCities citiesRepository;
    private final InterfClassDanger classDangerRepository;
    private final InterfMagazinTrash magazinTrashRepository;
    private final InterfNameDropAirTrash nameDropAirTrashRepository;
    private final InterfPhysStateTrash physStateTrashRepository;
    private final InterfShortDiscribeTechnology shortDiscribeTechnologyRepository;
    private final InterfTechnology technologyRepository;
    private final InterfMagasinFactory magasinFactoryRepository;
    private final InterfMyTrash myTrashRepository;
    private final InterfMyTrashCount myTrashCountRepository;
    private final InterfDropAir dropAirRepository;
    private final InterfNumberPhone numberPhoneRepository;
    private final InterfNumberPhoneCount numberPhoneCountRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Checking if database needs initialization...");

        // Проверяем, пустая ли БД (по основной таблице)
        if (magasinFactoryRepository.count() > 0) {
            if (myTrashCountRepository.count() == 0 && myTrashRepository.count() > 0) {
                log.info("Database has MyTrash without factory links. Backfilling MyTrashCount...");
                backfillMyTrashCounts();
            } else {
                log.info("Database already contains data. Skipping initialization.");
            }
            return;
        }

        log.info("Initializing test data for new entity structure...");

        // ==================== 1. ГЕОГРАФИЯ ====================

        // 1.1 Регионы
        List<Region> regions = Arrays.asList(
                Region.builder().name_region("Минская область").build(),
                Region.builder().name_region("Брестская область").build(),
                Region.builder().name_region("Гродненская область").build(),
                Region.builder().name_region("Гомельская область").build(),
                Region.builder().name_region("Витебская область").build(),
                Region.builder().name_region("Могилёвская область").build()
        );
        regions = regionRepository.saveAll(regions);
        log.info("Created {} regions", regions.size());

        // 1.2 Районы
        List<District> districts = Arrays.asList(
                District.builder().name_district("Минский район").build(),
                District.builder().name_district("Брестский район").build(),
                District.builder().name_district("Гродненский район").build(),
                District.builder().name_district("Гомельский район").build(),
                District.builder().name_district("Витебский район").build(),
                District.builder().name_district("Могилёвский район").build(),
                District.builder().name_district("Фрунзенский район").build(),
                District.builder().name_district("Ленинский район").build()
        );
        districts = districtRepository.saveAll(districts);
        log.info("Created {} districts", districts.size());

        // 1.3 Города
        List<Cities> cities = Arrays.asList(
                Cities.builder()
                        .id_region(regions.get(0))
                        .index("220000")
                        .id_district(districts.get(0))
                        .name_cities("Минск")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(1))
                        .index("224000")
                        .id_district(districts.get(1))
                        .name_cities("Брест")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(2))
                        .index("230000")
                        .id_district(districts.get(2))
                        .name_cities("Гродно")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(3))
                        .index("246000")
                        .id_district(districts.get(3))
                        .name_cities("Гомель")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(4))
                        .index("210000")
                        .id_district(districts.get(4))
                        .name_cities("Витебск")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(5))
                        .index("212000")
                        .id_district(districts.get(5))
                        .name_cities("Могилёв")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(0))
                        .index("222000")
                        .id_district(districts.get(6))
                        .name_cities("Заславль")
                        .build(),
                Cities.builder()
                        .id_region(regions.get(0))
                        .index("223000")
                        .id_district(districts.get(7))
                        .name_cities("Смолевичи")
                        .build()
        );
        cities = citiesRepository.saveAll(cities);
        log.info("Created {} cities", cities.size());

        // ==================== 2. СПРАВОЧНИКИ ====================

        // 2.1 Классы опасности
        List<ClassDanger> classDangers = Arrays.asList(
                ClassDanger.builder().class_danger(1).build(),
                ClassDanger.builder().class_danger(2).build(),
                ClassDanger.builder().class_danger(3).build(),
                ClassDanger.builder().class_danger(4).build(),
                ClassDanger.builder().class_danger(5).build()
        );
        classDangers = classDangerRepository.saveAll(classDangers);
        log.info("Created {} danger classes", classDangers.size());

        // 2.2 Справочник отходов (MagazinTrash)
        List<MagazinTrash> magazinTrashes = Arrays.asList(
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(0))
                        .code_trash(31401103)
                        .name_trash("Лом черных металлов не сортированный")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(1))
                        .code_trash(31401201)
                        .name_trash("Лом цветных металлов")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(2))
                        .code_trash(57101102)
                        .name_trash("Пластиковая упаковка")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(3))
                        .code_trash(35301101)
                        .name_trash("Отработанные масла")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(4))
                        .code_trash(35101103)
                        .name_trash("Строительный мусор")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(1))
                        .code_trash(92101101)
                        .name_trash("Макулатура")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(2))
                        .code_trash(47101102)
                        .name_trash("Отработанные шины")
                        .build(),
                MagazinTrash.builder()
                        .id_class_danger(classDangers.get(0))
                        .code_trash(91201101)
                        .name_trash("Стеклянная тара")
                        .build()
        );
        magazinTrashes = magazinTrashRepository.saveAll(magazinTrashes);
        log.info("Created {} trash types", magazinTrashes.size());

        // 2.3 Наименования выбросов в атмосферу
        List<NameDropAirTrash> nameDropAirTrashes = Arrays.asList(
                NameDropAirTrash.builder().name_drop_air_trash("Диоксид серы (SO₂)").build(),
                NameDropAirTrash.builder().name_drop_air_trash("Оксид углерода (CO)").build(),
                NameDropAirTrash.builder().name_drop_air_trash("Диоксид азота (NO₂)").build(),
                NameDropAirTrash.builder().name_drop_air_trash("Пыль неорганическая").build(),
                NameDropAirTrash.builder().name_drop_air_trash("Летучие органические соединения").build(),
                NameDropAirTrash.builder().name_drop_air_trash("Тяжелые металлы").build()
        );
        nameDropAirTrashes = nameDropAirTrashRepository.saveAll(nameDropAirTrashes);
        log.info("Created {} air drop names", nameDropAirTrashes.size());

        // 2.4 Физическое состояние отходов
        List<PhysStateTrash> physStateTrashes = Arrays.asList(
                PhysStateTrash.builder().name_group("Твердое").build(),
                PhysStateTrash.builder().name_group("Жидкое").build(),
                PhysStateTrash.builder().name_group("Пастообразное").build(),
                PhysStateTrash.builder().name_group("Сыпучее").build(),
                PhysStateTrash.builder().name_group("Газообразное").build()
        );
        physStateTrashes = physStateTrashRepository.saveAll(physStateTrashes);
        log.info("Created {} physical states", physStateTrashes.size());

        // 2.5 Краткое описание технологий
        List<ShortDiscribeTechnology> shortDiscribeTechnologies = Arrays.asList(
                ShortDiscribeTechnology.builder().technology("Термическая обработка").build(),
                ShortDiscribeTechnology.builder().technology("Механическая очистка").build(),
                ShortDiscribeTechnology.builder().technology("Биологическая очистка").build(),
                ShortDiscribeTechnology.builder().technology("Химическая нейтрализация").build(),
                ShortDiscribeTechnology.builder().technology("Сортировка и сепарация").build(),
                ShortDiscribeTechnology.builder().technology("Прессование и брикетирование").build()
        );
        shortDiscribeTechnologies = shortDiscribeTechnologyRepository.saveAll(shortDiscribeTechnologies);
        log.info("Created {} technology descriptions", shortDiscribeTechnologies.size());

        // 2.6 Технологии (связка класса опасности, отхода и физического состояния)
        List<Technology> technologies = Arrays.asList(
                Technology.builder()
                        .id_class_danger(classDangers.get(0))
                        .id_magazin_trash(magazinTrashes.get(0))
                        .id_phys_trash(physStateTrashes.get(0))
                        .build(),
                Technology.builder()
                        .id_class_danger(classDangers.get(1))
                        .id_magazin_trash(magazinTrashes.get(1))
                        .id_phys_trash(physStateTrashes.get(0))
                        .build(),
                Technology.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_magazin_trash(magazinTrashes.get(2))
                        .id_phys_trash(physStateTrashes.get(0))
                        .build(),
                Technology.builder()
                        .id_class_danger(classDangers.get(3))
                        .id_magazin_trash(magazinTrashes.get(3))
                        .id_phys_trash(physStateTrashes.get(1))
                        .build(),
                Technology.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_magazin_trash(magazinTrashes.get(4))
                        .id_phys_trash(physStateTrashes.get(0))
                        .build(),
                Technology.builder()
                        .id_class_danger(classDangers.get(1))
                        .id_magazin_trash(magazinTrashes.get(5))
                        .id_phys_trash(physStateTrashes.get(0))
                        .build()
        );
        technologies = technologyRepository.saveAll(technologies);
        log.info("Created {} technologies", technologies.size());

        // ==================== 3. ПРЕДПРИЯТИЯ (MagasinFactory) ====================

        List<MagasinFactory> magasinFactories = Arrays.asList(
                // Предприятие 1: Минский металлургический завод
                MagasinFactory.builder()
                        .id_registration("REG001")
                        .date_register(LocalDate.of(2010, 5, 15))
                        .id_cities(cities.get(0))
                        .id_short_discribe_technology(shortDiscribeTechnologies.get(0))
                        .id_technology(technologies.get(0))
                        .name_obj("Минский металлургический завод")
                        .name_own("ОАО Белорусский металлургический комбинат")
                        .address_own("г. Минск, ул. Тимирязева, 1")
                        .address_obj("г. Минск, ул. Промышленная, 15")
                        .develop_organization("Гипрометиз")
                        .confirmed_project("Проект №123/2010")
                        .date_approve(LocalDate.of(2010, 3, 20))
                        .conclusion_documentation(true)
                        .act_use("Акт ввода №45")
                        .requirements_acts("Соблюдать ПДК выбросов")
                        .obj_use_trash(true)
                        .obj_accept_trash(false)
                        .character_prod("Металлургия")
                        .project_power_yer("500000")
                        .project_power_hr("62.5")
                        .facticheskay_power("480000")
                        .YNP("123456789")
                        .value(100)
                        .build(),

                // Предприятие 2: Брестский завод по переработке пластика
                MagasinFactory.builder()
                        .id_registration("REG002")
                        .date_register(LocalDate.of(2015, 8, 20))
                        .id_cities(cities.get(1))
                        .id_short_discribe_technology(shortDiscribeTechnologies.get(4))
                        .id_technology(technologies.get(2))
                        .name_obj("Брестский завод переработки пластика")
                        .name_own("ООО Экопласт")
                        .address_own("г. Брест, ул. Московская, 10")
                        .address_obj("г. Брест, ул. Заводская, 25")
                        .develop_organization("ЭкоТехПроект")
                        .confirmed_project("Проект №456/2015")
                        .date_approve(LocalDate.of(2015, 6, 10))
                        .conclusion_documentation(true)
                        .act_use("Акт ввода №78")
                        .requirements_acts("Сортировка вторсырья")
                        .obj_use_trash(true)
                        .obj_accept_trash(true)
                        .character_prod("Переработка отходов")
                        .project_power_yer("120000")
                        .project_power_hr("15")
                        .facticheskay_power("115000")
                        .YNP("987654321")
                        .value(50)
                        .build(),

                // Предприятие 3: Гродненская ТЭЦ
                MagasinFactory.builder()
                        .id_registration("REG003")
                        .date_register(LocalDate.of(2005, 3, 10))
                        .id_cities(cities.get(2))
                        .id_short_discribe_technology(shortDiscribeTechnologies.get(0))
                        .id_technology(technologies.get(3))
                        .name_obj("Гродненская ТЭЦ-2")
                        .name_own("РУП Гродноэнерго")
                        .address_own("г. Гродно, ул. Энергетиков, 5")
                        .address_obj("г. Гродно, ул. Тепличная, 1")
                        .develop_organization("Белэнергопроект")
                        .confirmed_project("Проект №789/2005")
                        .date_approve(LocalDate.of(2005, 1, 15))
                        .conclusion_documentation(true)
                        .act_use("Акт ввода №12")
                        .requirements_acts("Очистка дымовых газов")
                        .obj_use_trash(false)
                        .obj_accept_trash(false)
                        .character_prod("Энергетика")
                        .project_power_yer("2000000")
                        .project_power_hr("250")
                        .facticheskay_power("1950000")
                        .YNP("555444333")
                        .value(200)
                        .build(),

                // Предприятие 4: Гомельский маслоэкстракционный завод
                MagasinFactory.builder()
                        .id_registration("REG004")
                        .date_register(LocalDate.of(2018, 11, 25))
                        .id_cities(cities.get(3))
                        .id_short_discribe_technology(shortDiscribeTechnologies.get(2))
                        .id_technology(technologies.get(4))
                        .name_obj("Гомельский МЭЗ")
                        .name_own("СП ООО Гомельмасло")
                        .address_own("г. Гомель, ул. Советская, 50")
                        .address_obj("г. Гомель, ул. Заводская, 100")
                        .develop_organization("Агропромпроект")
                        .confirmed_project("Проект №321/2018")
                        .date_approve(LocalDate.of(2018, 8, 30))
                        .conclusion_documentation(true)
                        .act_use("Акт ввода №56")
                        .requirements_acts("Очистка сточных вод")
                        .obj_use_trash(false)
                        .obj_accept_trash(false)
                        .character_prod("Пищевая промышленность")
                        .project_power_yer("300000")
                        .project_power_hr("37.5")
                        .facticheskay_power("285000")
                        .YNP("111222333")
                        .value(75)
                        .build(),

                // Предприятие 5: Витебский полигон ТБО
                MagasinFactory.builder()
                        .id_registration("REG005")
                        .date_register(LocalDate.of(2000, 6, 1))
                        .id_cities(cities.get(4))
                        .id_short_discribe_technology(shortDiscribeTechnologies.get(5))
                        .id_technology(technologies.get(5))
                        .name_obj("Витебский полигон ТБО")
                        .name_own("Витебский городской исполком")
                        .address_own("г. Витебск, ул. Ленина, 15")
                        .address_obj("Витебский р-н, д. Заречье")
                        .develop_organization("Горкомхоз")
                        .confirmed_project("Проект №555/2000")
                        .date_approve(LocalDate.of(2000, 3, 1))
                        .conclusion_documentation(true)
                        .act_use("Акт ввода №1")
                        .requirements_acts("Герметизация отходов")
                        .obj_use_trash(false)
                        .obj_accept_trash(true)
                        .character_prod("Размещение отходов")
                        .project_power_yer("400000")
                        .project_power_hr("50")
                        .facticheskay_power("380000")
                        .YNP("999888777")
                        .value(150)
                        .build(),

                // Предприятие 6: Могилёвский завод ЖБИ
                MagasinFactory.builder()
                        .id_registration("REG006")
                        .date_register(LocalDate.of(2012, 7, 18))
                        .id_cities(cities.get(5))
                        .id_short_discribe_technology(shortDiscribeTechnologies.get(1))
                        .id_technology(technologies.get(1))
                        .name_obj("Могилёвский завод ЖБИ")
                        .name_own("ОАО Могилёвстрой")
                        .address_own("г. Могилёв, пр. Мира, 30")
                        .address_obj("г. Могилёв, ул. Строителей, 45")
                        .develop_organization("Стройпроект")
                        .confirmed_project("Проект №777/2012")
                        .date_approve(LocalDate.of(2012, 5, 5))
                        .conclusion_documentation(true)
                        .act_use("Акт ввода №99")
                        .requirements_acts("Пылеулавливание")
                        .obj_use_trash(true)
                        .obj_accept_trash(false)
                        .character_prod("Строительные материалы")
                        .project_power_yer("80000")
                        .project_power_hr("10")
                        .facticheskay_power("75000")
                        .YNP("444555666")
                        .value(40)
                        .build()
        );

        magasinFactories = magasinFactoryRepository.saveAll(magasinFactories);
        log.info("Created {} enterprises (MagasinFactory)", magasinFactories.size());

        // ==================== 4. ОТХОДЫ ПРЕДПРИЯТИЙ (MyTrash) ====================

        List<MyTrash> myTrashes = Arrays.asList(
                // Отходы для Минского металлургического завода
                MyTrash.builder()
                        .id_class_danger(classDangers.get(0))
                        .id_magazin_trash(magazinTrashes.get(0))

                        .value_trash(12500.5f)
                        .build(),
                MyTrash.builder()
                        .id_class_danger(classDangers.get(1))
                        .id_magazin_trash(magazinTrashes.get(1))

                        .value_trash(250.3f)
                        .build(),

                // Отходы для Брестского завода пластика
                MyTrash.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_magazin_trash(magazinTrashes.get(2))

                        .value_trash(8000.0f)
                        .build(),
                MyTrash.builder()
                        .id_class_danger(classDangers.get(1))
                        .id_magazin_trash(magazinTrashes.get(5))

                        .value_trash(1500.0f)
                        .build(),

                // Отходы для Гродненской ТЭЦ
                MyTrash.builder()
                        .id_class_danger(classDangers.get(3))
                        .id_magazin_trash(magazinTrashes.get(3))

                        .value_trash(50000.0f)
                        .build(),
                MyTrash.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_magazin_trash(magazinTrashes.get(4))

                        .value_trash(35000.0f)
                        .build(),

                // Отходы для Гомельского МЭЗ
                MyTrash.builder()
                        .id_class_danger(classDangers.get(3))
                        .id_magazin_trash(magazinTrashes.get(3))
                        .value_trash(500.0f)
                        .build(),

                // Отходы для Витебского полигона
                MyTrash.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_magazin_trash(magazinTrashes.get(4))

                        .value_trash(280000.0f)
                        .build(),
                MyTrash.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_magazin_trash(magazinTrashes.get(2))

                        .value_trash(45000.0f)
                        .build(),

                // Отходы для Могилёвского завода ЖБИ
                MyTrash.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_magazin_trash(magazinTrashes.get(4))

                        .value_trash(12000.0f)
                        .build(),
                MyTrash.builder()
                        .id_class_danger(classDangers.get(0))
                        .id_magazin_trash(magazinTrashes.get(7))

                        .value_trash(3000.0f)
                        .build()
        );

        myTrashes = myTrashRepository.saveAll(myTrashes);
        log.info("Created {} trash records (MyTrash)", myTrashes.size());

        // ==================== 5. ВЫБРОСЫ В АТМОСФЕРУ (DropAir) ====================

        List<DropAir> dropAirs = Arrays.asList(
                // Выбросы Минского металлургического завода
                DropAir.builder()
                        .id_class_danger(classDangers.get(1))
                        .id_name_grope_air(nameDropAirTrashes.get(0))
                        .id_magasin_factory(magasinFactories.get(0))
                        .value_drop_trash(150.5f)
                        .build(),
                DropAir.builder()
                        .id_class_danger(classDangers.get(3))
                        .id_name_grope_air(nameDropAirTrashes.get(1))
                        .id_magasin_factory(magasinFactories.get(0))
                        .value_drop_trash(85.3f)
                        .build(),
                DropAir.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_name_grope_air(nameDropAirTrashes.get(2))
                        .id_magasin_factory(magasinFactories.get(0))
                        .value_drop_trash(45.2f)
                        .build(),

                // Выбросы Брестского завода пластика
                DropAir.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_name_grope_air(nameDropAirTrashes.get(3))
                        .id_magasin_factory(magasinFactories.get(1))
                        .value_drop_trash(30.0f)
                        .build(),
                DropAir.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_name_grope_air(nameDropAirTrashes.get(4))
                        .id_magasin_factory(magasinFactories.get(1))
                        .value_drop_trash(65.7f)
                        .build(),

                // Выбросы Гродненской ТЭЦ
                DropAir.builder()
                        .id_class_danger(classDangers.get(1))
                        .id_name_grope_air(nameDropAirTrashes.get(0))
                        .id_magasin_factory(magasinFactories.get(2))
                        .value_drop_trash(420.0f)
                        .build(),
                DropAir.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_name_grope_air(nameDropAirTrashes.get(2))
                        .id_magasin_factory(magasinFactories.get(2))
                        .value_drop_trash(180.5f)
                        .build(),
                DropAir.builder()
                        .id_class_danger(classDangers.get(3))
                        .id_name_grope_air(nameDropAirTrashes.get(1))
                        .id_magasin_factory(magasinFactories.get(2))
                        .value_drop_trash(95.8f)
                        .build(),

                // Выбросы Гомельского МЭЗ
                DropAir.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_name_grope_air(nameDropAirTrashes.get(3))
                        .id_magasin_factory(magasinFactories.get(3))
                        .value_drop_trash(12.5f)
                        .build(),

                // Выбросы Витебского полигона
                DropAir.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_name_grope_air(nameDropAirTrashes.get(3))
                        .id_magasin_factory(magasinFactories.get(4))
                        .value_drop_trash(85.0f)
                        .build(),
                DropAir.builder()
                        .id_class_danger(classDangers.get(2))
                        .id_name_grope_air(nameDropAirTrashes.get(4))
                        .id_magasin_factory(magasinFactories.get(4))
                        .value_drop_trash(45.3f)
                        .build(),

                // Выбросы Могилёвского завода ЖБИ
                DropAir.builder()
                        .id_class_danger(classDangers.get(4))
                        .id_name_grope_air(nameDropAirTrashes.get(3))
                        .id_magasin_factory(magasinFactories.get(5))
                        .value_drop_trash(55.0f)
                        .build()
        );

        dropAirs = dropAirRepository.saveAll(dropAirs);
        log.info("Created {} air emissions (DropAir)", dropAirs.size());

        // ==================== 6. НОМЕРА ТЕЛЕФОНОВ ====================

        List<NumberPhone> numberPhones = Arrays.asList(
                NumberPhone.builder().number("+375171234567").build(),
                NumberPhone.builder().number("+375171234568").build(),
                NumberPhone.builder().number("+375162345678").build(),
                NumberPhone.builder().number("+375152345678").build(),
                NumberPhone.builder().number("+375232345678").build(),
                NumberPhone.builder().number("+375212345678").build(),
                NumberPhone.builder().number("+375222345678").build(),
                NumberPhone.builder().number("+375177777777").build(),
                NumberPhone.builder().number("+375163333333").build()
        );
        numberPhones = numberPhoneRepository.saveAll(numberPhones);
        log.info("Created {} phone numbers", numberPhones.size());

        // ==================== 7. СВЯЗИ ПРЕДПРИЯТИЙ С ТЕЛЕФОНАМИ (NumberPhoneCount) ====================

        List<NumberPhoneCount> numberPhoneCounts = Arrays.asList(
                // Телефоны Минского металлургического завода
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(0))
                        .id_object_place_trash(magasinFactories.get(0))
                        .ur_ob(1)
                        .build(),
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(1))
                        .id_object_place_trash(magasinFactories.get(0))
                        .ur_ob(0)
                        .build(),

                // Телефоны Брестского завода пластика
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(2))
                        .id_object_place_trash(magasinFactories.get(1))
                        .ur_ob(1)
                        .build(),
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(8))
                        .id_object_place_trash(magasinFactories.get(1))
                        .ur_ob(0)
                        .build(),

                // Телефоны Гродненской ТЭЦ
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(3))
                        .id_object_place_trash(magasinFactories.get(2))
                        .ur_ob(1)
                        .build(),

                // Телефоны Гомельского МЭЗ
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(4))
                        .id_object_place_trash(magasinFactories.get(3))
                        .ur_ob(1)
                        .build(),

                // Телефоны Витебского полигона
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(5))
                        .id_object_place_trash(magasinFactories.get(4))
                        .ur_ob(1)
                        .build(),
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(7))
                        .id_object_place_trash(magasinFactories.get(4))
                        .ur_ob(0)
                        .build(),

                // Телефоны Могилёвского завода ЖБИ
                NumberPhoneCount.builder()
                        .id_phone_number(numberPhones.get(6))
                        .id_object_place_trash(magasinFactories.get(5))
                        .ur_ob(1)
                        .build()
        );

        numberPhoneCountRepository.saveAll(numberPhoneCounts);
        log.info("Created {} phone-enterprise links", numberPhoneCounts.size());

        // ==================== 8. СВЯЗИ ОТХОДОВ С ПРЕДПРИЯТИЯМИ (MyTrashCount) ====================

        List<MyTrashCount> myTrashCounts = Arrays.asList(
                // Минский металлургический завод
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(0))
                        .id_object_place_trash(magasinFactories.get(0))
                        .build(),
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(1))
                        .id_object_place_trash(magasinFactories.get(0))
                        .build(),

                // Брестский завод переработки пластика
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(2))
                        .id_object_place_trash(magasinFactories.get(1))
                        .build(),
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(3))
                        .id_object_place_trash(magasinFactories.get(1))
                        .build(),

                // Гродненская ТЭЦ
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(4))
                        .id_object_place_trash(magasinFactories.get(2))
                        .build(),
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(5))
                        .id_object_place_trash(magasinFactories.get(2))
                        .build(),

                // Гомельский МЭЗ
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(6))
                        .id_object_place_trash(magasinFactories.get(3))
                        .build(),

                // Витебский полигон ТБО
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(7))
                        .id_object_place_trash(magasinFactories.get(4))
                        .build(),
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(8))
                        .id_object_place_trash(magasinFactories.get(4))
                        .build(),

                // Могилёвский завод ЖБИ
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(9))
                        .id_object_place_trash(magasinFactories.get(5))
                        .build(),
                MyTrashCount.builder()
                        .id_my_trash(myTrashes.get(10))
                        .id_object_place_trash(magasinFactories.get(5))
                        .build()
        );
        myTrashCountRepository.saveAll(myTrashCounts);
        log.info("Created {} myTrashCount links", myTrashCounts.size());

        log.info("========================================");
        log.info("Database initialization completed successfully!");
        log.info("Created:");
        log.info("  - {} regions", regions.size());
        log.info("  - {} districts", districts.size());
        log.info("  - {} cities", cities.size());
        log.info("  - {} danger classes", classDangers.size());
        log.info("  - {} trash types", magazinTrashes.size());
        log.info("  - {} air drop names", nameDropAirTrashes.size());
        log.info("  - {} physical states", physStateTrashes.size());
        log.info("  - {} tech descriptions", shortDiscribeTechnologies.size());
        log.info("  - {} technologies", technologies.size());
        log.info("  - {} enterprises", magasinFactories.size());
        log.info("  - {} trash records", myTrashes.size());
        log.info("  - {} air emissions", dropAirs.size());
        log.info("  - {} phone numbers", numberPhones.size());
        log.info("  - {} phone links", numberPhoneCounts.size());
        log.info("  - {} myTrashCount links", myTrashCounts.size());
        log.info("========================================");
    }

    /**
     * Связи отход → предприятие по порядку тестовых данных (11 отходов, 6 предприятий).
     */
    private void backfillMyTrashCounts() {
        List<MyTrash> trashes = myTrashRepository.findAll().stream()
                .sorted(Comparator.comparing(MyTrash::getId_my_trash))
                .toList();
        List<MagasinFactory> factories = magasinFactoryRepository.findAll().stream()
                .sorted(Comparator.comparing(MagasinFactory::getId_magasin_factory))
                .toList();

        int[][] trashToFactoryIndex = {
                {0, 0}, {1, 0},
                {2, 1}, {3, 1},
                {4, 2}, {5, 2},
                {6, 3},
                {7, 4}, {8, 4},
                {9, 5}, {10, 5}
        };

        if (trashes.size() < trashToFactoryIndex.length || factories.isEmpty()) {
            log.warn("Cannot backfill MyTrashCount: trashes={}, factories={}",
                    trashes.size(), factories.size());
            return;
        }

        List<MyTrashCount> links = Arrays.stream(trashToFactoryIndex)
                .map(pair -> MyTrashCount.builder()
                        .id_my_trash(trashes.get(pair[0]))
                        .id_object_place_trash(factories.get(Math.min(pair[1], factories.size() - 1)))
                        .build())
                .toList();

        myTrashCountRepository.saveAll(links);
        log.info("Backfilled {} myTrashCount links", links.size());
    }
}