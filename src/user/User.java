package user;

import entertainment.Genre;
import video.Video;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Butilca Rares
 */
public abstract class User {
    /**
     * Wraps a video along with some information that relates a user with a specific video
     */
    public final class VideoWrapper {

        private Video video;
        private int viewCount;
        // Maps seasons with user ratings, (!) for movies, the only entry will be for "season" 0
        private HashMap<Integer, Double> ratings = new HashMap<>();

        public VideoWrapper(final Video video, final int viewCount) {
            this.video = video;
            this.viewCount = viewCount;
        }

        /**
         * Increments the number of views
         */
        public void view() {
            viewCount++;
        }

        public int getViews() {
            return viewCount;
        }

        public HashMap<Integer, Double> getRatings() {
            return ratings;
        }
    }

    protected ArrayList<VideoWrapper> favoriteVideos;
    protected ArrayList<VideoWrapper> viewedVideos = new ArrayList<>();
    protected String username;

    protected User(final String username, final ArrayList<Video> favoriteVideos,
                   final Map<Video, Integer> viewedVideos) {
        this.username = username;
        // Viewed videos are mapped with their number of views from the user
        this.viewedVideos.addAll(wrapList(viewedVideos));
        this.favoriteVideos = findVideoList(favoriteVideos);

        // Actualise viewed videos information in the database

        for (VideoWrapper videoWr : this.viewedVideos) {
            videoWr.video.incrementViews(videoWr.viewCount);
        }

        for (Video video : favoriteVideos) {
            video.newFavorite();
        }
    }

    /**
     * Executes a recommendation
     * @param videos list of videos present in the database
     * @param type the type of recommendation required
     * @param genre the genre required(only useful when the type is search)
     * @return the video recommended
     */
    public abstract String recommend(ArrayList<Video> videos, String type,
                                     Genre genre);

    /**
     * (!) the string can be null if the constructor was given a null username as parameter
     * @return a string containing the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * (!) list can be null or empty
     * @return an array list containing the favorite videos
     */
    public ArrayList<VideoWrapper> getFavoriteVideos() {
        return favoriteVideos;
    }

    /**
     * (!) list can be empty but not null
     * @return an array list containing the viewed videos
     */
    public ArrayList<VideoWrapper> getViewedVideos() {
        return viewedVideos;
    }

    // crates a list of wrappers from a Hashmap<Video, no_views>
    private ArrayList<VideoWrapper> wrapList(final Map<Video, Integer> videos) {
        ArrayList<VideoWrapper> wrappers = new ArrayList<>();

        for (Video video : videos.keySet()) {
            wrappers.add(new VideoWrapper(video, videos.get(video)));
        }

        return wrappers;
    }

    /**
     * Searches for a list of videos in the viewed list
     * <p>
     * The given list must have viewed videos, otherwise the returned list will have null elements
     * </p>
     * @param videos list of videos to be searched
     * @return list of wrappers belonging to found videos
     */
    public ArrayList<VideoWrapper> findVideoList(final ArrayList<Video> videos) {
        ArrayList<VideoWrapper> wrappers = new ArrayList<>();

        for (Video video : videos) {
            wrappers.add(findVideo(video, viewedVideos));
        }

        return wrappers;
    }

    /**
     * Searches for a video in a wrapper list
     * @param video video to be searched
     * @param list list of wrappers where the search occurs
     * @return wrapper of the found video or null if there is no match
     */
    public VideoWrapper findVideo(final Video video, final ArrayList<VideoWrapper> list) {
        for (VideoWrapper videoWr : list) {
            if (videoWr.video == video) {
                return videoWr;
            }
        }

        return null;
    }

    /**
     * Calculates the number of ratings the user has given
     * @return no ratings
     */
    public int getRatingCount() {
        int count = 0;

        for (VideoWrapper videoWr : viewedVideos) {
            if (videoWr.ratings.size() != 0) {
                count++;
            }
        }

        return count;
    }

    /**
     * Checks whether the user has seen a given video
     */
    public boolean hasViewed(final Video video) {
        return findVideo(video, viewedVideos) == null ? false : true;
    }

    /**
     * The users are represented by their usernames
     * @return username
     */
    @Override
    public String toString() {
        return getUsername();
    }
}
