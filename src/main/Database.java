package main;

import actor.Actor;
import common.Constants;
import entertainment.Genre;
import fileio.Input;
import fileio.MovieInputData;
import fileio.Writer;
import fileio.SerialInputData;
import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.ActorInputData;
import user.PremiumUser;
import user.StandardUser;
import user.User;
import utils.CommandsAssistant;
import utils.QueryAssistant;
import utils.Utils;
import video.Video;
import video.Movie;
import video.Show;

import org.json.simple.JSONArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Butilca Rares
 * Database data and functions in one object (main object)
 */
public final class Database {
    private final ArrayList<Actor> actors = new ArrayList<>();
    // All videos(movies and shows) in one array
    private final ArrayList<Video> videos = new ArrayList<>();
    // Chose to make these 2 Video arrays for functions compatibility reasons
    private final ArrayList<Video> movies = new ArrayList<>();
    private final ArrayList<Video> shows = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    // find*Something* functions search *something* by name

    private Video findVideo(final String title) {
        for (Video video : videos) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }

        return null;
    }

    private Actor findActor(final String name) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }

        return null;
    }

    private User findUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    // Reads the classes and stores the information in the database
    private void classesInput(final Input input) {
        for (MovieInputData data : input.getMovies()) {
            ArrayList<Genre> genres = new ArrayList<>();
            ArrayList<Actor> cast = new ArrayList<>();

            for (String genre : data.getGenres()) {
                genres.add(Utils.stringToGenre(genre));
            }

            for (String name : data.getCast()) {
                cast.add(findActor(name));
            }

            movies.add(new Movie(data.getTitle(), data.getYear(), genres, data.getDuration(),
                    cast));
        }

        for (SerialInputData data : input.getSerials()) {
            ArrayList<Genre> genres = new ArrayList<>();
            ArrayList<Actor> cast = new ArrayList<>();

            for (String genre : data.getGenres()) {
                genres.add(Utils.stringToGenre(genre));
            }

            for (String name : data.getCast()) {
                cast.add(findActor(name));
            }

            shows.add(new Show(data.getTitle(), data.getYear(), genres, data.getSeasons(), cast));
        }

        videos.addAll(movies);
        videos.addAll(shows);

        for (UserInputData data : input.getUsers()) {
            // Maps seen videos with view count
            Map<Video, Integer> history = new HashMap<>();
            ArrayList<Video> favorites = new ArrayList<>();

            for (String title : data.getHistory().keySet()) {
                history.put(findVideo(title), data.getHistory().get(title));
            }

            for (String title : data.getFavoriteMovies()) {
                favorites.add(findVideo(title));
            }

            if (data.getSubscriptionType().equals(Constants.PREMIUM_SUBSCRIPTION)) {
                users.add(new PremiumUser(data.getUsername(), favorites, history));
            } else {
                users.add(new StandardUser(data.getUsername(), favorites, history));
            }
        }

        for (ActorInputData data : input.getActors()) {
            ArrayList<Video> filmography = new ArrayList<>();

            // Counting only the videos present in the database, since the others can be ignored
            for (String title : data.getFilmography()) {
                if (findVideo(title) != null) {
                    filmography.add(findVideo(title));
                }
            }

            actors.add(new Actor(data.getName(), data.getCareerDescription(), filmography,
                    data.getAwards()));
        }
    }

    // Reads all actions, executes them and prints the results
    private void actionExec(final Input input, final Writer writer, final JSONArray array) {
        // Output string
        StringBuilder message = new StringBuilder();

        for (ActionInputData data : input.getCommands()) {
            // "Clears" the old message from the buffer
            message.setLength(0);

            switch (data.getActionType()) {
                case Constants.COMMAND:
                    switch (data.getType()) {
                        case Constants.FAVORITE:
                            message.append(CommandsAssistant.favorite(findVideo(data.getTitle()),
                                    findUser(data.getUsername())));
                            break;
                        case Constants.VIEW:
                            message.append(CommandsAssistant.view(findVideo(data.getTitle()),
                                    findUser(data.getUsername())));
                            break;
                        case Constants.RATING:
                            message.append(CommandsAssistant.rating(findVideo(data.getTitle()),
                                    data.getSeasonNumber(), data.getGrade(),
                                    findUser(data.getUsername())));
                            break;
                        default:
                            break;
                    }

                    break;
                case Constants.QUERY:
                    switch (data.getObjectType()) {
                        case Constants.ACTORS:
                            message.append(QueryAssistant.actorQuery(actors, data.getNumber(),
                                    data.getFilters(), Utils.orderAsInt(data.getSortType()),
                                    data.getCriteria()));
                            break;
                        case Constants.MOVIES:
                            message.append(QueryAssistant.videoQuery(movies, data.getNumber(),
                                    data.getFilters(), Utils.orderAsInt(data.getSortType()),
                                    data.getCriteria()));
                            break;
                        case Constants.SHOWS:
                            message.append(QueryAssistant.videoQuery(shows, data.getNumber(),
                                    data.getFilters(), Utils.orderAsInt(data.getSortType()),
                                    data.getCriteria()));
                            break;
                        case Constants.USERS:
                            message.append(QueryAssistant.userQuery(users, data.getNumber(),
                                    Utils.orderAsInt(data.getSortType()), data.getCriteria()));
                            break;
                        default:
                            break;
                    }

                    break;
                case Constants.RECOMMENDATION:
                    Genre genre;
                    // stringToGenre doesn't accept null strings
                    if (data.getGenre() == null) {
                        genre = null;
                    } else {
                        genre = Utils.stringToGenre(data.getGenre());
                    }

                    message.append(findUser(data.getUsername()).recommend(videos,
                            data.getType(), genre));
                    break;
                default:
                    break;
            }

            try {
                array.add(writer.writeFile(data.getActionId(), null, message.toString()));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * The "main" function(entry point)
     */
    public void run(final Input input, final Writer writer, final JSONArray array) {
        classesInput(input);
        actionExec(input, writer, array);
    }
}
