package ru.snptech.businessbanyabot.model.search;

public enum SlideDirection {
    NEXT, PREVIOUS;

    public Integer nextPosition(Integer current, Integer total) {
        return switch (this) {
            case NEXT -> {
                var next = current + 1;

                if (next >= total) yield 0;

                yield next;
            }

            case PREVIOUS -> {
                var next = current - 1;

                if (next < 0) yield total - 1;

                yield next;
            }
        };
    }
}
