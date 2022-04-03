//trb comm
package utils;

import actor.Actor;
import common.Constants;
import user.User;
import video.Video;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Butilca Rares
 */
public final class QueryAssistant {
    private QueryAssistant() {

    }

    /**
     * Filters actors by a list of given filters
     * @return a separate list of actors that fall under all criterias
     */
    public static ArrayList<Actor> filterActors(final ArrayList<Actor> actors,
                                                final List<List<String>> filters) {
        ArrayList<Actor> filteredActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (!actor.hasInDescription(filters.get(Constants.WORDS_FILTER))) {
                continue;
            }

            if (!actor.hasAwards(Utils.stringsToAwards(filters.get(Constants.AWARDS_FILTER)))) {
                continue;
            }

            filteredActors.add(actor);
        }

        return filteredActors;
    }

    /**
     * Filters videos by a list of given filterss
     * @return a separate list of videos that fall under all criterias
     */
    public static ArrayList<Video> filterVideos(final ArrayList<Video> videos,
                                                final List<List<String>> filters) {
        ArrayList<Video> filteredVideos = new ArrayList<>();

        for (Video video : videos) {
            if (filters.get(Constants.YEAR_FILTER).get(0) != null && video.getYear()
                    != Integer.valueOf(filters.get(Constants.YEAR_FILTER).get(0))) {
                continue;
            }

            if (!video.getGenres().containsAll(Utils.stringsToGenres(filters.get(Constants.
                    GENRE_FILTER)))) {
                continue;
            }

            filteredVideos.add(video);
        }

        return filteredVideos;
    }

    /**
     * Sorts actors by a given criteria and a given order
     * @param criteria average grade or number of awards
     * @param order 1 -> ascendant, -1 -> descendant
     * @return same list but sorted
     */
    public static ArrayList<Actor> sortActors(final ArrayList<Actor> actors, final String criteria,
                                              final int order) {
        if (criteria.equals(Constants.AVERAGE)) {
            actors.sort(new Comparator<>() {
                @Override
                public int compare(final Actor a, final Actor b) {
                    if (a.getAverageRating() == b.getAverageRating()) {
                        return a.getName().compareTo(b.getName()) * order;
                    }

                    return (int) Math.signum(a.getAverageRating() - b.getAverageRating()) * order;
                }
            });
            // Get rid of unrated actors early
            for (int i = 0; i < actors.size(); i++) {
                if (actors.get(i).getAverageRating() == 0) {
                    actors.remove(i);
                    i--;
                }
            }
        }

        if (criteria.equals(Constants.AWARDS)) {
            actors.sort(new Comparator<>() {
                @Override
                public int compare(final Actor a, final Actor b) {
                    if (a.getAwardsCount() == b.getAwardsCount()) {
                        return a.getName().compareTo(b.getName()) * order;
                    }

                    return (a.getAwardsCount() - b.getAwardsCount()) * order;
                }
            });
        }

        if (criteria.equals(Constants.FILTER_DESCRIPTIONS)) {
            actors.sort(new Comparator<>() {
                @Override
                public int compare(final Actor a, final Actor b) {
                    return a.getName().compareTo(b.getName()) * order;
                }
            });
        }

        return actors;
    }

    /**
     * Sorts videos by a given criteria and a given order
     * @param criteria average grade, number of favorites, duration or number of views
     * @param order 1 -> ascendant, -1 -> descendant
     * @return same list but sorted
     */
    public static ArrayList<Video> sortVideos(final ArrayList<Video> videos, final String criteria,
                                              final int order) {
        if (criteria.equals(Constants.RATINGS)) {
            videos.sort(new Comparator<>() {
                @Override
                public int compare(final Video a, final Video b) {
                    if (a.getRating() == b.getRating()) {
                        return a.getTitle().compareTo(b.getTitle()) * order;
                    }

                    return (int) Math.signum(a.getRating() - b.getRating()) * order;
                }
            });
            // Get rid of unrated videos early
            for (int i = 0; i < videos.size(); i++) {
                if (videos.get(i).getRating() == 0) {
                    videos.remove(i);
                    i--;
                }
            }
        }

        if (criteria.equals(Constants.FAVORITE)) {
            videos.sort(new Comparator<>() {
                @Override
                public int compare(final Video a, final Video b) {
                    if (a.getFavoritesCount() == b.getFavoritesCount()) {
                        return a.getTitle().compareTo(b.getTitle()) * order;
                    }

                    return (a.getFavoritesCount() - b.getFavoritesCount()) * order;
                }
            });
            // Get rid of videos who are nobody's favorite early
            for (int i = 0; i < videos.size(); i++) {
                if (videos.get(i).getFavoritesCount() == 0) {
                    videos.remove(i);
                    i--;
                }
            }
        }

        if (criteria.equals(Constants.LONGEST)) {
            videos.sort(new Comparator<>() {
                @Override
                public int compare(final Video a, final Video b) {
                    if (a.getDuration() == b.getDuration()) {
                        return a.getTitle().compareTo(b.getTitle()) * order;
                    }

                    return (a.getDuration() - b.getDuration()) * order;
                }
            });
        }

        if (criteria.equals(Constants.MOST_VIEWED)) {
            videos.sort(new Comparator<>() {
                @Override
                public int compare(final Video a, final Video b) {
                    if (a.getViews() == b.getViews()) {
                        return a.getTitle().compareTo(b.getTitle()) * order;
                    }

                    return (a.getViews() - b.getViews()) * order;
                }
            });
            // Get rid of unviewed videos early
            for (int i = 0; i < videos.size(); i++) {
                if (videos.get(i).getViews() == 0) {
                    videos.remove(i);
                    i--;
                }
            }
        }

        return videos;
    }

    /**
     * Sorts users by a given criteria and a given order
     * @param criteria is always no given ratings
     * @param order 1 -> ascendant, -1 -> descendant
     * @return separate list of sorted users
     */
    public static ArrayList<User> sortUsers(final ArrayList<User> users, final String criteria,
                                            final int order) {
        ArrayList<User> sortedUsers = new ArrayList<>(users);

        if (criteria.equals(Constants.NUM_RATINGS)) {
            sortedUsers.sort(new Comparator<User>() {
                @Override
                public int compare(final User a, final User b) {
                    if (a.getRatingCount() == b.getRatingCount()) {
                        return a.getUsername().compareTo(b.getUsername()) * order;
                    }

                    return (a.getRatingCount() - b.getRatingCount()) * order;
                }
            });
            // Get rid of users who have given no ratings early
            for (int i = 0; i < sortedUsers.size(); i++) {
                if (sortedUsers.get(i).getRatingCount() == 0) {
                    sortedUsers.remove(i);
                    i--;
                }
            }
        }

        return sortedUsers;
    }

    /**
     * Does a query for actors
     * @param actors list of actors from the database
     * @param number number of returned actors
     * @param filters used for filtering
     * @param order used for sorting
     * @param criteria sorting criteria
     * @return list of actors from the query
     */
    public static String actorQuery(final ArrayList<Actor> actors, final int number,
                                    final List<List<String>> filters, final int order,
                                    final String criteria) {
        ArrayList<Actor> returnedArray = filterActors(actors, filters);
        sortActors(returnedArray, criteria, order);

        if (number < returnedArray.size()) {
            returnedArray.subList(number, returnedArray.size()).clear();
        }

        return "Query result: " + Utils.arrayToString(returnedArray);
    }

    /**
     * Does a query for movies/shows/videos
     * @param videos list of videos/movies/shows from the database
     * @param number number of returned videos
     * @param filters used for filtering
     * @param order used for sorting
     * @param criteria sorting criteria
     * @return list of videos/movies/shows from the query
     */
    public static String videoQuery(final ArrayList<Video> videos, final int number,
                                    final List<List<String>> filters, final int order,
                                    final String criteria) {
        ArrayList<Video> returnedArray = filterVideos(videos, filters);
        sortVideos(returnedArray, criteria, order);

        if (number < returnedArray.size()) {
            returnedArray.subList(number, returnedArray.size()).clear();
        }

        return "Query result: " + Utils.arrayToString(returnedArray);
    }

    /**
     * Does a query for users
     * @param users list of users from the database
     * @param number number of returned users
     * @param order used for sorting
     * @param criteria sorting criteria
     * @return list of users from the query
     */
    public static String userQuery(final ArrayList<User> users, final int number,
                                   final int order, final String criteria) {
        ArrayList<User> returnedArray = sortUsers(users, criteria, order);

        if (number < returnedArray.size()) {
            returnedArray.subList(number, returnedArray.size()).clear();
        }

        return "Query result: " + Utils.arrayToString(returnedArray);
    }
}
