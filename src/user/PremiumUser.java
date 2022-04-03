package user;

import entertainment.Genre;
import utils.RecommendationsAssistant;
import video.Video;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Butilca Rares
 */
public final class PremiumUser extends User {
    public PremiumUser(final String username, final ArrayList<Video> favoriteVideos,
                       final Map<Video, Integer> viewedVideos) {
        super(username, favoriteVideos, viewedVideos);
    }

    @Override
    public String recommend(final ArrayList<Video> videos, final String type,
                            final Genre genre) {
        return RecommendationsAssistant.premiumRecommender(videos, type, genre, this);
    }
}
