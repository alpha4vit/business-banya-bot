package ru.snptech.ritualbitrixbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.DealActionStatus;
import ru.snptech.ritualbitrixbot.entity.DealFlow;
import ru.snptech.ritualbitrixbot.entity.DealStatus;
import ru.snptech.ritualbitrixbot.entity.Region;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.events.dto.NewDealEvent;
import ru.snptech.ritualbitrixbot.events.dto.PhoneReceivedEvent;
import ru.snptech.ritualbitrixbot.model.FuneralDealData;
import ru.snptech.ritualbitrixbot.model.ShopDealData;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.repository.RegionRepository;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository dealRepository;
    private final RegionRepository regionRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void initShopDeal(ShopDealData data) {
        var region = getRegion(data.region());
        if (region == null) {
            log.warn("Пропущена заявка: {}\n\nНе найден регион", data);
            return;
        }
        var regionUsers = telegramUserRepository.findAllByRegion(region);
        if (regionUsers.isEmpty()) {
            log.warn("Пропущена заявка: {}\n\nНет пользователей в этом регионе", data);
            return;
        }
        var targetUser = getUsersWithDealsCount(regionUsers).entrySet().stream()
                .min(Map.Entry.comparingByValue()).get().getKey();
        Deal deal = new Deal();
        deal.setId(data.id());
        deal.setSource(data.source());
        deal.setSource2(data.source2());
        deal.setDealType(data.dealType());
        deal.setComment(data.comment());
        deal.setContactName(data.clientName());
        deal.setContactPhone(data.clientPhone());
        deal.setTelegramUser(targetUser);
        deal.setFlow(DealFlow.SHOP);
        deal.setDealActionStatus(DealActionStatus.WAITING_APPROVAL);
        deal.setComment(Optional.ofNullable(deal.getComment()).map(value -> value.endsWith("_") ? value.substring(0, value.length() - 1) : value).orElse(""));
        dealRepository.save(deal);
        sendNewDealEvent(deal, targetUser);
    }

    public void initFuneralDeal(FuneralDealData data) {
        var region = getRegion(data.region());
        if (region == null) {
            log.warn("Пропущена заявка: {}\n\nНе найден регион", data);
            return;
        }
        var regionUsers = telegramUserRepository.findAllByRegion(region);
        if (regionUsers.isEmpty()) {
            log.warn("Пропущена заявка: {}\n\nНет пользователей в этом регионе", data);
            return;
        }
        var targetUser = getUsersWithDealsCount(regionUsers).entrySet().stream()
                .min(Map.Entry.comparingByValue()).get().getKey();
        Deal deal = new Deal();
        deal.setId(data.id());
        deal.setSource(data.source());
        deal.setSource2(data.source2());
        deal.setDealType(data.dealType());
        deal.setComment(data.comment());
        deal.setAddress(data.address());
        deal.setCity(data.city());
        deal.setCustomerName(data.customerName());
        deal.setDeceasedSurname(data.deceasedSurname());
        deal.setTelegramUser(targetUser);
        deal.setFlow(DealFlow.FUNERAL);
        deal.setDealActionStatus(DealActionStatus.WAITING_APPROVAL);
        deal.setComment(Optional.ofNullable(deal.getComment()).map(value -> value.endsWith("_") ? value.substring(0, value.length() - 1) : value).orElse(""));
        dealRepository.save(deal);
        sendNewDealEvent(deal, targetUser);
    }

    public void updateDealPhone(String dealId, String contactName, String contactPhone) {
        var deal = dealRepository.findDealById(dealId);
        if (deal == null) return;
        deal.setContactName(contactName);
        deal.setContactPhone(contactPhone);
        deal.setRequestedPhone(contactPhone);
        dealRepository.save(deal);
        applicationEventPublisher.publishEvent(new PhoneReceivedEvent(this, dealId));
    }

    private Region getRegion(String regionName) {
        return regionRepository.findByName(regionName);
    }

    private Map<TelegramUser, Long> getUsersWithDealsCount(List<TelegramUser> telegramUsers) {
        return telegramUsers.stream().collect(Collectors.toMap(
                Function.identity(),
                dealRepository::countAllByTelegramUser
        ));
    }

    private void sendNewDealEvent(Deal deal, TelegramUser targetUser) {
        applicationEventPublisher.publishEvent(new NewDealEvent(
                this,
                deal.getId(),
                deal.getSource(),
                deal.getSource2(),
                deal.getDealType(),
                targetUser.getChatId().toString()
        ));
    }

    public void finishDeal(String dealId, boolean success) {
        var deal = dealRepository.findDealById(dealId);
        deal.setStatus(success ? DealStatus.SUCCESS : DealStatus.FAILURE);
        dealRepository.save(deal);
    }

}
