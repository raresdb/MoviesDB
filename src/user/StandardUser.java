package user;

import entertainment.Genre;
import utils.RecommendationsAssistant;
import video.Video;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Butilca Rares
 */
public final class StandardUser extends User {
    public StandardUser(final String username, final ArrayList<Video> favoriteVideos,
                        final Map<Video, Integer> viewedVideos) {
        super(username, favoriteVideos, viewedVideos);
    }

    @Override
    public String recommend(final ArrayList<Video> videos, final String type,
                            final Genre genre) {
        return RecommendationsAssistant.standardRecommender(videos, type, this);
    }
}
