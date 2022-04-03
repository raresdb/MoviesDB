package video;

import actor.Actor;
import entertainment.Genre;
import java.util.ArrayList;

/**
 * @author Butilca Rares
 */
public abstract class Video {
    protected String title;
    protected int year;
    protected ArrayList<Genre> genres = new ArrayList<>();
    protected int duration;
    protected int favoritesCount;
    protected int views;
    protected ArrayList<Actor> cast = new ArrayList<>();

    protected Video(final String title, final int year, final ArrayList<Genre> genres,
                    final ArrayList<Actor> cast) {
        this.title = title;
        this.year = year;
        this.genres.addAll(genres);
        this.favoritesCount = 0;
        this.views = 0;
        this.cast.addAll(cast);
    }

    /**
     * Adds a rating to a season -> 0 for movies
     * @param rating
     * @param season
     */
    public abstract void newRating(double rating, int season);

    /**
     * @return average rating
     */
    public abstract double getRating();

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Counts the favorites
     * @return favorites number
     */
    public int getFavoritesCount() {
        return favoritesCount;
    }

    /**
     * @return views
     */
    public int getViews() {
        return views;
    }

    /**
     * @return duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return genre
     */
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    /**
     * increment favorites
     */
    public void newFavorite() {
        favoritesCount++;
    }

    /**
     * increment views
     */
    public void newView() {
        views++;
    }

    /**
     * increments views with n
     * @param n
     */
    public void incrementViews(final int n) {
        views += n;
    }

    /**
     * @return title
     */
    @Override
    public String toString() {
        return title;
    }
}
