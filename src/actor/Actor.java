package actor;

import video.Video;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Butilca Rares
 */
public final class Actor {

    private final String name;
    private final String careerDescription;
    private final ArrayList<Video> filmography = new ArrayList<>();
    // Associates ActorsAwards owned by the actor with their count
    private final Map<ActorsAwards, Integer> awards = new HashMap<>();

    public Actor(final String name, final String careerDescription, final ArrayList<Video> videos,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography.addAll(videos);
        this.awards.putAll(awards);
    }

    /**
     * Calculates the average rating of the actor
     * @return average rating
     */
    public double getAverageRating() {
        double average = 0;
        int videosWithoutRating = 0;

        for (Video video : filmography) {
            if (video.getRating() == 0) {
                videosWithoutRating++;
            }

            average += video.getRating();
        }

        // Case where no video had rating => actor has no rating(0)
        if (average == 0) {
            return average;
        }

        average = average / (filmography.size() - videosWithoutRating);
        return average;
    }

    /**
     * Sums up all awards of the actor
     * @return total number of awards
     */
    public int getAwardsCount() {
        int count = 0;

        for (ActorsAwards award : awards.keySet()) {
            count += awards.get(award);
        }

        return count;
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if the actor has all the awards from the list presented as parameter.
     * @param list list of awards
     */
    public boolean hasAwards(final List<ActorsAwards> list) {
        if (list == null) {
            return true;
        }

        for (ActorsAwards award : list) {
            if (!awards.containsKey(award)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether the actor has a list of keywords in their description.
     * @param list list of keywords
     */
    public boolean hasInDescription(final List<String> list) {
        if (list == null) {
            return true;
        }

        for (String kw : list) {
            // Used a regex for checking words(not just substrings)
            if (!careerDescription.toLowerCase().matches("(?s)(^|.*[^a-zA-Z])"
                    + kw.toLowerCase() + "($|[^a-zA-Z].*)")) {
                return false;
            }
        }

        return true;
    }

    //actors are represented by their names
    @Override
    public String toString() {
        return name;
    }
}
