package utils;

import common.Constants;
import entertainment.Genre;
import user.User;
import video.Video;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Butilca Rares
 */
public final class RecommendationsAssistant {
    private RecommendationsAssistant() {

    }
    //all users

    /**
     * Standard recommendation for a user
     * @param videos list of videos from database
     * @return recommended video or null if none found
     */
    public static Video standard(final ArrayList<Video> videos, final User user) {
        for (Video video : videos) {
            if (!user.hasViewed(video)) {
                return video;
            }
        }

        return null;
    }

    /**
     * Best Unseen recommendation for a user
     * @param videos list of videos from database
     * @return recommended video or null if none found
     */
    public static Video bestUnseen(final ArrayList<Video> videos, final User user) {
        ArrayList<Video> sortedVideos = new ArrayList<>(videos);
        HashMap<Video, Integer> videoIndexMap = new HashMap<>();

        for (int i = 0; i < videos.size(); i++) {
            videoIndexMap.put(videos.get(i), i);
        }

        sortedVideos.sort(new Comparator<>() {
            @Override
            public int compare(final Video a, final Video b) {
                if (b.getRating() == a.getRating()) {
                    return videoIndexMap.get(a) - videoIndexMap.get(b);
                }

                return (int) Math.signum(b.getRating() - a.getRating());
            }
        });
        for (Video video : sortedVideos) {
            if (!user.hasViewed(video)) {
                return video;
            }
        }

        return null;
    }

//premium users

    /**
     * Most popular video recommendation
     * @param videos list of videos from database
     * @return recommended video or null if none found
     */
    public static Video popular(final ArrayList<Video> videos, final User user) {
        ArrayList<Video> sortedVideos = new ArrayList<>(videos);
        HashMap<Genre, Integer> genresPopularity = Utils.mapGenreByPopularity(videos);
        HashMap<Video, Integer> videoIndexMap = new HashMap<>();

        for (int i = 0; i < videos.size(); i++) {
            videoIndexMap.put(videos.get(i), i);
        }

        sortedVideos.sort(new Comparator<>() {
            @Override
            public int compare(final Video a, final Video b) {
                ArrayList<Genre> sortedGenresA = new ArrayList<>(a.getGenres());
                ArrayList<Genre> sortedGenresB = new ArrayList<>(b.getGenres());
                 sortedGenresA.sort(new Comparator<Genre>() {
                    @Override
                    public int compare(final Genre c, final Genre d) {
                        return genresPopularity.get(d) - genresPopularity.get(c);
                    }
                });
                sortedGenresB.sort(new Comparator<>() {
                    @Override
                    public int compare(final Genre c, final Genre d) {
                        return genresPopularity.get(d) - genresPopularity.get(c);
                    }
                });
                if (genresPopularity.get(sortedGenresB.get(0))
                        == genresPopularity.get(sortedGenresA.get(0))) {
                    return videoIndexMap.get(a) - videoIndexMap.get(b);
                }

                return genresPopularity.get(sortedGenresB.get(0))
                        - genresPopularity.get(sortedGenresA.get(0));
            }
        });
        for (Video video : sortedVideos) {
            if (!user.hasViewed(video)) {
                return video;
            }
        }

        return null;
    }

    /**
     * Most favorite video recommendation
     * @param videos list of videos from database
     * @return recommended video or null if none found
     */
    public static Video favorite(final ArrayList<Video> videos, final User user) {
        ArrayList<Video> sortedVideos = new ArrayList<>(videos);
        HashMap<Video, Integer> videoIndexMap = new HashMap<>();

        for (int i = 0; i < videos.size(); i++) {
            videoIndexMap.put(videos.get(i), i);
        }

        sortedVideos.sort(new Comparator<>() {
            @Override
            public int compare(final Video a, final Video b) {
                if (b.getFavoritesCount() == a.getFavoritesCount()) {
                    return videoIndexMap.get(a) - videoIndexMap.get(b);
                }

                return b.getFavoritesCount() - a.getFavoritesCount();
            }
        });
        for (Video video : sortedVideos) {
            if (!user.hasViewed(video) && video.getFavoritesCount() != 0) {
                return video;
            }
        }

        return null;
    }

    /**
     * Search recommendation
     * @param videos list of videos from database
     * @param genre genre for the search criteria
     * @return recommended videos or null if none found
     */
    public static ArrayList<Video> search(final ArrayList<Video> videos, final User user,
                                          final Genre genre) {
        ArrayList<Video> sortedVideos = new ArrayList<>();

        for (Video video : videos) {
            if (video.getGenres().contains(genre) && !user.hasViewed(video)) {
                sortedVideos.add(video);
            }
        }
        sortedVideos.sort(new Comparator<>() {
            @Override
            public int compare(final Video a, final Video b) {
                if (b.getRating() != a.getRating()) {
                    return (int) Math.signum(a.getRating() - b.getRating());
                } else {
                    return a.getTitle().compareTo(b.getTitle());
                }
            }
        });
        return sortedVideos;
    }

    /**
     * Converts a string type given as parameter into the form that is necessary for printing
     * @param type type as parameter
     * @return type as output
     */
    public static String printableType(final String type) {
        return switch (type) {
            case Constants.STANDARD -> "Standard";
            case Constants.BEST_UNSEEN -> "BestRatedUnseen";
            case Constants.POPULAR -> "Popular";
            case Constants.FAVORITE -> "Favorite";
            case Constants.SEARCH -> "Search";
            default -> null;
        };
    }

    /**
     * Chooses the proper recommendation function for standard users
     * @param videos list of videos from database
     * @param type recommendation type given as string
     * @return message for the output
     */
    public static String standardRecommender(final ArrayList<Video> videos, final String type,
                                     final User user) {
        Video returnedVideo = null;

        if (type.equals(Constants.STANDARD)) {
            returnedVideo = standard(videos, user);
        }

        if (type.equals(Constants.BEST_UNSEEN)) {
            returnedVideo = bestUnseen(videos, user);
        }

        if (returnedVideo == null) {
            return printableType(type)
                    + "Recommendation cannot be applied!";
        }

        return printableType(type) + "Recommendation result: "
                + returnedVideo;
    }

    /**
     * Chooses the proper function for premium recommendation
     * @param videos list of videos from database
     * @param type type as string
     * @param genre genre for the search recc
     * @return message for the output
     */
    public static String premiumRecommender(final ArrayList<Video> videos, final String type,
                                     final Genre genre, final User user) {
        ArrayList<Video> returnedArray = null;
        Video returnedVideo = null;

        if (type.equals(Constants.STANDARD) || type.equals(Constants.BEST_UNSEEN)) {
            return standardRecommender(videos, type, user);
        }

        if (type.equals(Constants.POPULAR)) {
            returnedVideo = RecommendationsAssistant.popular(videos, user);
        }

        if (type.equals(Constants.FAVORITE)) {
            returnedVideo = RecommendationsAssistant.favorite(videos, user);
        }

        if (type.equals(Constants.SEARCH)) {
            returnedArray = RecommendationsAssistant.search(videos, user, genre);
        }

        if (returnedArray != null && returnedArray.size() != 0) {
            return RecommendationsAssistant.printableType(type) + "Recommendation result: "
                    + Utils.arrayToString(returnedArray);
        }

        if (returnedVideo != null) {
            return RecommendationsAssistant.printableType(type) + "Recommendation result: "
                    + returnedVideo;
        }

        return RecommendationsAssistant.printableType(type) + "Recommendation cannot be "
                + "applied!";
    }
}
