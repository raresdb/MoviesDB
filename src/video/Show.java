package video;

import actor.Actor;
import entertainment.Genre;
import entertainment.Season;
import java.util.ArrayList;

/**
 * @author Butilca Rares
 */
public final class Show extends Video {
    private final ArrayList<Season> seasons = new ArrayList<>();

    public Show(final String title, final int year, final ArrayList<Genre> genres,
                final ArrayList<Season> seasons, final ArrayList<Actor> cast) {
        super(title, year, genres, cast);
        this.seasons.addAll(seasons);
        this.duration = 0;

        for (Season season : seasons) {
            this.duration += season.getDuration();
        }
    }

    /**
     * Adds a rating to a season of the show
     */
    @Override
    public void newRating(final double rating, final int season) {
        seasons.get(season - 1).getRatings().add(rating);
    }

    /**
     * @return average rating of the show
     */
    public double getRating() {
        double avgRating = 0;

        if (seasons.size() == 0) {
            return avgRating;
        }

        for (Season season : seasons) {
            double avg = 0;

            if (season.getRatings().size() == 0) {
                continue;
            }

            for (Double rating : season.getRatings()) {
                avg += rating;
            }

            avg = avg / season.getRatings().size();
            avgRating += avg;
        }

        avgRating = avgRating / seasons.size();
        return avgRating;
    }
}
