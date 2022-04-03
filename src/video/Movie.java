//trb comm
package video;

import actor.Actor;
import entertainment.Genre;

import java.util.ArrayList;

/**
 * @author Butilca Rares
 */
public final class Movie extends Video {
    private final ArrayList<Double> ratings = new ArrayList<>();

    public Movie(final String title, final int year, final ArrayList<Genre> genres,
                 final int duration, final ArrayList<Actor> cast) {
        super(title, year, genres, cast);
        this.duration = duration;
    }

    @Override
    /**
     * Adds a rating
     */
    public void newRating(final double rating, final int season) {
        ratings.add(rating);
    }

    /**
     * @return average rating
     */
    public double getRating() {
        double avg = 0;

        if (ratings.size() == 0) {
            return avg;
        }

        for (Double rating : ratings) {
            avg += rating;
        }

        avg = avg / ratings.size();
        return avg;
    }
}
