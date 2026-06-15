package com.example.eco_service.services;


import com.example.eco_service.dto.main_dto.WasteTypeReportDto;
import com.example.eco_service.entities.*;
import com.example.eco_service.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final InterfMagazinTrash magazinTrashRepository;
    private final InterfMyTrash myTrashRepository;
    private final InterfMyTrashCount myTrashCountRepository;
    private final InterfMagasinFactory magasinFactoryRepository;

    /**
     * Получить все данные для отчёта по типам отходов
     */
    public List<WasteTypeReportDto> getAllForReport() {
        log.info("Fetching all waste types for report");

        List<MagazinTrash> wasteTypes = magazinTrashRepository.findAll();
        List<WasteTypeReportDto> reportData = new ArrayList<>();

        for (MagazinTrash wasteType : wasteTypes) {
            reportData.add(convertToReportDto(wasteType));
        }

        reportData.sort(Comparator.comparing(WasteTypeReportDto::getCodeTrash));
        log.info("Generated report data for {} waste types", reportData.size());
        return reportData;
    }

    /**
     * Получить данные для отчёта по конкретному типу отхода
     */
    public WasteTypeReportDto getForReportById(Long id) {
        log.info("Fetching waste type report data for id: {}", id);

        MagazinTrash wasteType = magazinTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + id));

        return convertToReportDto(wasteType);
    }

    /**
     * Получить данные для отчёта по коду отхода
     */
    public WasteTypeReportDto getForReportByCode(Integer codeTrash) {
        log.info("Fetching waste type report data for code: {}", codeTrash);

        if (codeTrash == null) {
            throw new RuntimeException("Code trash cannot be null");
        }

        MagazinTrash wasteType = magazinTrashRepository.findAll().stream()
                .filter(w -> w.getCode_trash() == codeTrash)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with code: " + codeTrash));

        return convertToReportDto(wasteType);
    }

    /**
     * Получить данные для отчёта по классу опасности
     */
    public List<WasteTypeReportDto> getForReportByClassDanger(Integer classDanger) {
        log.info("Fetching waste type report data for class danger: {}", classDanger);

        if (classDanger == null) {
            throw new RuntimeException("Class danger cannot be null");
        }

        List<MagazinTrash> wasteTypes = magazinTrashRepository.findAll().stream()
                .filter(w -> w.getId_class_danger() != null &&
                        w.getId_class_danger().getClass_danger() == classDanger)
                .collect(Collectors.toList());

        return wasteTypes.stream()
                .map(this::convertToReportDto)
                .sorted(Comparator.comparing(WasteTypeReportDto::getCodeTrash))
                .collect(Collectors.toList());
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private String getPhoneByUrRole(MagasinFactory factory, boolean isObject) {
        if (factory.getNumberPhoneCounts() == null || factory.getNumberPhoneCounts().isEmpty()) {
            return "";
        }

        return factory.getNumberPhoneCounts().stream()
                .filter(npc -> npc.getId_phone_number() != null)
                .filter(npc -> {
                    int urOb = npc.getUr_ob();
                    if (isObject) {
                        return urOb == 1 || urOb == 3;
                    } else {
                        return urOb == 0 || urOb == 3;
                    }
                })
                .map(npc -> npc.getId_phone_number().getNumber())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    /**
     * Найти все предприятия, работающие с данным типом отхода
     */
    private List<MagasinFactory> findFactoriesByWasteType(MagazinTrash wasteType) {
        // Находим все записи MyTrash для данного типа отхода
        List<MyTrash> myTrashRecords = myTrashRepository.findAll().stream()
                .filter(mt -> mt.getId_magazin_trash() != null &&
                        mt.getId_magazin_trash().getId_magazin_trash().equals(wasteType.getId_magazin_trash()))
                .collect(Collectors.toList());

        // Через MyTrashCount находим предприятия
        List<MagasinFactory> factories = new ArrayList<>();
        for (MyTrash myTrash : myTrashRecords) {
            List<MyTrashCount> counts = myTrashCountRepository.findAllByMyTrashId(myTrash.getId_my_trash());
            for (MyTrashCount count : counts) {
                if (count.getId_object_place_trash() != null) {
                    factories.add(count.getId_object_place_trash());
                }
            }
        }

        return factories.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Найти количество отхода для конкретного предприятия
     * (через MyTrashCount, но так как там нет количества, возвращаем значение из MyTrash)
     */
    private Float getWasteValueForFactory(MagazinTrash wasteType, MagasinFactory factory) {
        // Находим запись MyTrash для данного типа отхода
        MyTrash myTrash = myTrashRepository.findAll().stream()
                .filter(mt -> mt.getId_magazin_trash() != null &&
                        mt.getId_magazin_trash().getId_magazin_trash().equals(wasteType.getId_magazin_trash()))
                .findFirst()
                .orElse(null);

        if (myTrash == null) {
            return 0f;
        }

        // Проверяем, есть ли связь с предприятием через MyTrashCount
        Optional<MyTrashCount> count = myTrashCountRepository.findByMyTrashAndFactory(
                myTrash.getId_my_trash(), factory.getId_magasin_factory());

        if (count.isPresent()) {
            return myTrash.getValue_trash();
        }

        return 0f;
    }

    /**
     * Преобразовать сущность в DTO для отчёта
     */
    private WasteTypeReportDto convertToReportDto(MagazinTrash wasteType) {
        Integer classDanger = wasteType.getId_class_danger() != null ?
                wasteType.getId_class_danger().getClass_danger() : null;

        List<MagasinFactory> factories = findFactoriesByWasteType(wasteType);
        List<WasteTypeReportDto.FactoryForWasteReportDto> factoryDtos = new ArrayList<>();

        for (MagasinFactory factory : factories) {
            factoryDtos.add(WasteTypeReportDto.FactoryForWasteReportDto.builder()
                    .id(factory.getId_magasin_factory())
                    .nameObj(factory.getName_obj())
                    .addressObj(factory.getAddress_obj())
                    .phoneObj(getPhoneByUrRole(factory, true))
                    .nameOwn(factory.getName_own())
                    .addressOwn(factory.getAddress_own())
                    .phoneOwn(getPhoneByUrRole(factory, false))
                    .objUseTrash(factory.getObj_use_trash())
                    .objAcceptTrash(factory.getObj_accept_trash())
                    .valueTrash(getWasteValueForFactory(wasteType, factory))
                    .registrationNumber(factory.getId_registration())
                    .ynp(factory.getYNP())
                    .build());
        }

        factoryDtos.sort(Comparator.comparing(WasteTypeReportDto.FactoryForWasteReportDto::getNameObj,
                Comparator.nullsLast(String::compareTo)));

        return WasteTypeReportDto.builder()
                .id(wasteType.getId_magazin_trash())
                .codeTrash(wasteType.getCode_trash())
                .nameTrash(wasteType.getName_trash())
                .classDanger(classDanger)
                .factories(factoryDtos)
                .build();
    }
}