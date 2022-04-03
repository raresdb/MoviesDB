package utils;

import user.User;
import video.Video;

/**
 * @author Butilca Rares
 */
public final class CommandsAssistant {
    private CommandsAssistant() {

    }

    /**
     * Adds a video in the favorites list of a given user
     * @return error or success message for the output
     */
    public static String favorite(final Video video, final User user) {
        User.VideoWrapper videoWr = user.findVideo(video, user.getViewedVideos());
        if (videoWr == null) {
            return "error -> " + video.getTitle() + " is not seen";
        }
        if (user.getFavoriteVideos().contains(videoWr)) {
            return "error -> " + video.getTitle() + " is already in favourite list";
        }

        user.getFavoriteVideos().add(videoWr);
        video.newFavorite();
        return "success -> " + video.getTitle() + " was added as favourite";
    }

    /**
     * Marks a video as seen or increases the number of views for a given user
     * @return error or success message for the output
     */
    public static String view(final Video video, final User user) {
        User.VideoWrapper videoWr = user.findVideo(video, user.getViewedVideos());

        if (videoWr == null) {
            videoWr = user.new VideoWrapper(video, 1);
            user.getViewedVideos().add(videoWr);
        }  else {
            videoWr.view();
        }

        video.newView();
        return "success -> " + video.getTitle() + " was viewed with total views of "
                + videoWr.getViews();
    }

    /**
     * Gives a rating from a user to a video
     * @param season of the video(if it is a show) or 0 for movies
     * @param rating grade
     * @return error or success message for the output
     */
    public static String rating(final Video video, final int season, final double rating,
                                final User user) {
        User.VideoWrapper videoWr = user.findVideo(video, user.getViewedVideos());

        if (videoWr == null) {
            return "error -> " + video.getTitle() + " is not seen";
        }

        if (videoWr.getRatings().containsKey(season)) {
            return "error -> " + video.getTitle() + " has been already rated";
        }

        videoWr.getRatings().put(season, rating);
        video.newRating(rating, season);
        return "success -> " + video.getTitle() + " was rated with " + rating + " by "
                + user.getUsername();
    }
}
