package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.SurveyQuestion;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.repository.SurveyQuestionRepository;

import java.util.List;
import java.util.function.Predicate;

@Component
public class FakeSurveyQuestionRepository implements SurveyQuestionRepository {

    private final List<SurveyQuestion> questions = List.of(
        new SurveyQuestion(
            1L,
            "Р\\. Имя и Фамилия",
            SurveyScenarioStep.FIO
        ),
        new SurveyQuestion(
            2L,
            "Укажите свой аккаунт в социальных сетях \\(Instagram\\, VK\\, Facebook\\, можно указать все\\, можно какую\\-то одну\\, где вы чаще всего бываете\\)",
            SurveyScenarioStep.SOCIAL_MEDIA
        ),
        new SurveyQuestion(
            3L,
            "Укажите вашу дату рождения",
            SurveyScenarioStep.DATE_OF_BIRTH
        ),
        new SurveyQuestion(
            4L,
            "Укажите из какого Вы города",
            SurveyScenarioStep.CITY
        ),
        new SurveyQuestion(
            5L,
            "Укажите сколько лет вы занимаетесь бизнесом\\?",
            SurveyScenarioStep.YEARS_OF_EXPERIENCE
        ),
        new SurveyQuestion(
            6L,
            "Опишите кратко свой \\(свои\\) текущий \\(ие\\) бизнес \\- проект \\(ы\\)\\. Ответив на следующие вопросы по каждому бизнесу: Что продаёте \\\\ производите, или какие услуги оказывает\\? Численность штатных сотрудников\\? Годовой оборот в деньгах\\? Сколько существует данный бизнес в месяц \\\\ годах\\? Вы в нем собственник \\\\ партнёр \\\\ самозанятый эксперт\\? Если есть сайт \\\\ лендинг \\\\ аккаунт в соцсетях на свой бизнес \\- укажите обязательно\\. \\(на все вопросы ответьте по каждому бизнес\\- проекту\\)",
            SurveyScenarioStep.BUSINESS_DESCRIPTION
        ),
        new SurveyQuestion(
            7L,
            "Сколько выводите на себя \\\\ свою семью \\\\ сколько забираете на свои нужды ежемесячно\\? \\(Ваш текущий уровень дохода \\(в среднем за прошедшие 12 месяцев\\)\\?\\)",
            SurveyScenarioStep.WITHDRAWALS
        ),
        new SurveyQuestion(
            8L,
            "Ваше семейное положение\\?",
            SurveyScenarioStep.FAMILY_STATUS
        ),
        new SurveyQuestion(
            9L,
            "Есть ли дети\\?",
            SurveyScenarioStep.CHILDREN_COUNT
        ),
        new SurveyQuestion(
            10L,
            "Занятие спортом\\. Опишите каким \\(и\\) видом \\(ами\\) спорта занимаетесь сейчас\\? \\(например: самостоятельные ежедневные тренировки, бегаю на дистанцию 5 км 3 раза в день, играю в футбол по субботам\\)\\. Укажите чем занимаетесь, как часто и с какой периодичностью",
            SurveyScenarioStep.SPORT_INTERESTS
        ),
        new SurveyQuestion(
            11L,
            "Опишите от 5 до 10 своих убеждений\\? Какие фразы характеризуют вас, как человека, как лидера\\? \\(например: 1\\) Относись к людям так, как хочешь, чтобы относились к тебе, 2\\) Живи здесь и сейчас\\; 3\\) Иерархия возможна только с самим собой: я стал лучше, чем вчера или нет\\?\\)",
            SurveyScenarioStep.BELIEFS
        ),
        new SurveyQuestion(
            12L,
            "Опишите 5 любимых музыкальных исполнителей\\. Например: Олег Газманов, Бутырка, Modern Talking и т\\.д\\.",
            SurveyScenarioStep.MUSIC_SINGERS
        ),
        new SurveyQuestion(
            13L,
            "Напишите 5 ключевых слов в своей жизни\\? \\(5 слов, которыми вы можете охарактеризовать себя, слова, которые для вас не просто звук, а имеют под собой смысл, историю, опыт, ценности\\)\\. Например: Любовь\\; Семья\\; Дружба\\; Здоровье\\; Результат\\.",
            SurveyScenarioStep.CRUCIAL_WORDS
        ),
        new SurveyQuestion(
            14L,
            "Напишите 5 своих любимых фильмов\\? Например: Собачье сердце\\; На грани\\; Кун\\-фу Панда 3 часть\\; Офицеры\\; В бой идут одни старики",
            SurveyScenarioStep.FILMS
        ),
        new SurveyQuestion(
            15L,
            "Напишите 5 своих сильных сторон, качеств\\. Например: Спокойствие\\; Умею думать через результат \\(решениями, а не проблемами\\)\\; Целеустремленность\\; Жажда постижения новых знаний и приобретения нового опыта",
            SurveyScenarioStep.STRENGTHS
        ),
        new SurveyQuestion(
            16L,
            "Опишите 5 своих побед \\(что для вас является ценным достижением в жизни\\)\\. Например: Закончил школу с золотой медалью\\; в 18 лет стал жить самостоятельно, в 27 \\- заработал свой первый миллион, отец 3\\-х детей и т\\.д\\.",
            SurveyScenarioStep.VICTORIES
        ),
        new SurveyQuestion(
            17L,
            "Опишите 5 своих поражений \\(люблю поесть, так и не преодолел тягу к вкусной еде, пережил предательство друга \\- партнера по бизнесу, потерял в бизнесе 4,0 млн рублей на самом старте и т\\.д\\.\\)",
            SurveyScenarioStep.DEFEATS
        ),
        new SurveyQuestion(
            18L,
            "Напишите 5 своих учителей и почему вы их таковыми считаете\\? Например: И\\. Сталин \\- научил добиваться поставленной цели, Михаил Федоренко \\- научил думать и мыслить масштабно на уровне страны, мира и т\\.д\\. Это могут быть герои из книг, автобиографий, реальные учителя, родители и т\\.д\\.",
            SurveyScenarioStep.TEACHERS
        ),
        new SurveyQuestion(
            19L,
            "Напишите свою большую и глобальную цель на ближайшие 5\\-10\\-25 лет что хотите достичь, реализовать, сделать, построить и т\\.д\\.? \\(период вы определите сами, но чем на более длительный период тем лучше\\)",
            SurveyScenarioStep.GOALS
        ),
        new SurveyQuestion(
            20L,
            "Опишите свой актив \\(в чем вы сильны и можете помочь другим людям, что можете им дать\\? в чем посоветовать\\? в чем уровень эксперта и умудренного опытом человека\\? С какими проблемами можете помочь в решении\\?\\)",
            SurveyScenarioStep.ACTIVE_SIDE
        ),
        new SurveyQuestion(
            21L,
            "Опишите свой пассив \\(в решении какой задачи нужна помощь\\? каких навыков не хватает\\? о чем хотели бы узнать\\? в чем прокачаться\\?\\)",
            SurveyScenarioStep.PASSIVE_SIDE
        ),
        new SurveyQuestion(
            22L,
            "Кем или по чьей рекомендации вы пришли\\?",
            SurveyScenarioStep.REFERRER
        )
    );

    @Override
    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    @Override
    public SurveyQuestion getByScenarioStep(final SurveyScenarioStep step) {
        return getByPredicate((it) -> it.getScenarioStep().equals(step));
    }

    @Override
    public SurveyQuestion getById(final Long id) {
        return getByPredicate((it) -> it.getNumber().equals(id));
    }

    private SurveyQuestion getByPredicate(final Predicate<SurveyQuestion> predicate) {
        return questions.stream()
            .filter(predicate)
            .findFirst()
            .orElseThrow(() -> new BusinessBanyaDomainLogicException.SURVEY_NOT_FOUND());
    }
}
