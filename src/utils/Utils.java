//trb comm
package utils;

import actor.ActorsAwards;
import common.Constants;
import entertainment.Genre;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import video.Video;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import  java.util.Arrays;
import java.util.List;

/**
 * The class contains static methods that helps with parsing.
 * @author Butilca Rares
 */
public final class Utils {

    /**
     * Transforms a string into an enum
     * @param genre of video
     * @return an Genre Enum
     */
    public static Genre stringToGenre(final String genre) {
        return switch (genre.toLowerCase()) {
            case "action" -> Genre.ACTION;
            case "adventure" -> Genre.ADVENTURE;
            case "drama" -> Genre.DRAMA;
            case "comedy" -> Genre.COMEDY;
            case "crime" -> Genre.CRIME;
            case "romance" -> Genre.ROMANCE;
            case "war" -> Genre.WAR;
            case "history" -> Genre.HISTORY;
            case "thriller" -> Genre.THRILLER;
            case "mystery" -> Genre.MYSTERY;
            case "family" -> Genre.FAMILY;
            case "horror" -> Genre.HORROR;
            case "fantasy" -> Genre.FANTASY;
            case "science fiction" -> Genre.SCIENCE_FICTION;
            case "action & adventure" -> Genre.ACTION_ADVENTURE;
            case "sci-fi & fantasy" -> Genre.SCI_FI_FANTASY;
            case "animation" -> Genre.ANIMATION;
            case "kids" -> Genre.KIDS;
            case "western" -> Genre.WESTERN;
            case "tv movie" -> Genre.TV_MOVIE;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     * @param award for actors
     * @return an ActorsAwards Enum
     */
    public static ActorsAwards stringToAwards(final String award) {
        return switch (award) {
            case "BEST_SCREENPLAY" -> ActorsAwards.BEST_SCREENPLAY;
            case "BEST_SUPPORTING_ACTOR" -> ActorsAwards.BEST_SUPPORTING_ACTOR;
            case "BEST_DIRECTOR" -> ActorsAwards.BEST_DIRECTOR;
            case "BEST_PERFORMANCE" -> ActorsAwards.BEST_PERFORMANCE;
            case "PEOPLE_CHOICE_AWARD" -> ActorsAwards.PEOPLE_CHOICE_AWARD;
            default -> null;
        };
    }

    /**
     * Transforms an array of JSON's into an array of strings
     * @param array of JSONs
     * @return a list of strings
     */
    public static ArrayList<String> convertJSONArray(final JSONArray array) {
        if (array != null) {
            ArrayList<String> finalArray = new ArrayList<>();
            for (Object object : array) {
                finalArray.add((String) object);
            }
            return finalArray;
        } else {
            return null;
        }
    }

    /**
     * Transforms an array of JSON's into a map
     * @param jsonActors array of JSONs
     * @return a map with ActorsAwardsa as key and Integer as value
     */
    public static Map<ActorsAwards, Integer> convertAwards(final JSONArray jsonActors) {
        Map<ActorsAwards, Integer> awards = new LinkedHashMap<>();

        for (Object iterator : jsonActors) {
            awards.put(stringToAwards((String) ((JSONObject) iterator).get(Constants.AWARD_TYPE)),
                    Integer.parseInt(((JSONObject) iterator).get(Constants.NUMBER_OF_AWARDS)
                            .toString()));
        }

        return awards;
    }

    /**
     * Transforms an array of JSON's into a map
     * @param movies array of JSONs
     * @return a map with String as key and Integer as value
     */
    public static Map<String, Integer> watchedMovie(final JSONArray movies) {
        Map<String, Integer> mapVideos = new LinkedHashMap<>();

        if (movies != null) {
            for (Object movie : movies) {
                mapVideos.put((String) ((JSONObject) movie).get(Constants.NAME),
                        Integer.parseInt(((JSONObject) movie).get(Constants.NUMBER_VIEWS)
                                .toString()));
            }
        } else {
            System.out.println("NU ESTE VIZIONAT NICIUN FILM");
        }

        return mapVideos;
    }

    /**
     * Maps all genres with number of views
     * @param videos list of videos from database
     * @return map of genres
     */
    public static HashMap<Genre, Integer> mapGenreByPopularity(final ArrayList<Video> videos) {
        HashMap<Genre, Integer> genresPopularity = new HashMap<>();
        ArrayList<Genre> genres = new ArrayList(Arrays.asList(Genre.values()));

        for (Genre genre : genres) {
            genresPopularity.put(genre, 0);
        }

        for (Video video : videos) {
            for (Genre genre : video.getGenres()) {
                genresPopularity.replace(genre, genresPopularity.get(genre) + video.getViews());
            }
        }

        return genresPopularity;
    }

    /**
     * Converts order from string to int
     */
    public static int orderAsInt(final String order) {
        if (order.equals("desc")) {
            return -1;
        }

        return 1;
    }

    /**
     * Creates list of genres from list of strings
     */
    public static ArrayList<Genre> stringsToGenres(final List<String> genres) {
        ArrayList<Genre> formattedGenres = new ArrayList<Genre>();

        if (genres == null) {
            return null;
        }

        for (String genre : genres) {
            if (genre != null) {
                formattedGenres.add(stringToGenre(genre));
            }
        }

        return formattedGenres;
    }

    /**
     * Creates list of awards from list of strings
     */
    public static ArrayList<ActorsAwards> stringsToAwards(final List<String> awards) {
        ArrayList<ActorsAwards> formattedAwards = new ArrayList<ActorsAwards>();

        if (awards == null) {
            return null;
        }

        for (String award : awards) {
            formattedAwards.add(stringToAwards(award));
        }

        return formattedAwards;
    }

    /**
     * A to string function for array lists
     */
    public static String arrayToString(final ArrayList array) {
        if (array.size() == 0) {
            return "[]";
        }

        StringBuilder string = new StringBuilder("[");

        for (Object obj : array) {
            string.append(obj.toString() + ", ");
        }

        string.replace(string.length() - 2, string.length(), "]");
        return string.toString();
    }
}
