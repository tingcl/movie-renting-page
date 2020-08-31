package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.MovieService;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class MovieRecords {
    public static SearchMovieResponse movieSearchQuery(SearchMovieRequest searchMovieRequest) {
        int initialParamIndex = 1;
        int title = 0, genre = 0, year = 0, director = 0;
        String query;
        if (!searchMovieRequest.getGenre().equals("null")) {
            query = "SELECT DISTINCT movies.id, title, year, director, rating, numVotes, hidden " +
                    "FROM movies, genres, genres_in_movies, ratings " +
                    "WHERE movies.id = genres_in_movies.movieId AND movies.id = ratings.movieId AND genres_in_movies.genreId = genres.id";
        } else {
            query = "SELECT DISTINCT movies.id, title, year, director, rating, numVotes, hidden " +
                    "FROM movies, ratings " +
                    "WHERE movies.id = ratings.movieId";
        }
        ServiceLogger.LOGGER.info("Request to query for movies...");
        if (!searchMovieRequest.getTitle().equals("null")&&!searchMovieRequest.getTitle().equals("")) {
            query += " AND title LIKE ?";
            title = initialParamIndex;
            initialParamIndex++;
        }
        if (!searchMovieRequest.getGenre().equals("null")&&!searchMovieRequest.getGenre().equals("")) {
            query += " AND name LIKE ?";
            genre = initialParamIndex;
            initialParamIndex++;
        }
        if (searchMovieRequest.getYear() != 0) {
            query += " AND year LIKE ?";
            year = initialParamIndex;
            initialParamIndex++;
        }
        if (!searchMovieRequest.getDirector().equals("null")&&!searchMovieRequest.getDirector().equals("")) {
            query += " AND director LIKE ?";
            director = initialParamIndex;
            initialParamIndex++;
        }
        query += " AND movies.hidden LIKE " + (searchMovieRequest.getHidden() ? 1 : 0) + " ORDER BY " + searchMovieRequest.getOrderby()
                + " " + searchMovieRequest.getDirection() + ", title asc" + " LIMIT " + searchMovieRequest.getLimit() + " OFFSET " +
                searchMovieRequest.getOffset() + ";";
        try {
            ServiceLogger.LOGGER.info("Items in prepared statement.......");
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            if (title != 0) {
                ServiceLogger.LOGGER.info("title: " + searchMovieRequest.getTitle());
                ServiceLogger.LOGGER.info("num: " + title);
                ps.setString(title, "%" + searchMovieRequest.getTitle() + "%");
            }
            if (genre != 0){
                ServiceLogger.LOGGER.info("genre: " + searchMovieRequest.getGenre());
                ServiceLogger.LOGGER.info("num: " + genre);
                ps.setString(genre, searchMovieRequest.getGenre());
            }
            if (year != 0){
                ServiceLogger.LOGGER.info("year: " + searchMovieRequest.getYear());
                ServiceLogger.LOGGER.info("num: " + year);
                ps.setInt(year, searchMovieRequest.getYear());
            }
            if (director != 0){
                ServiceLogger.LOGGER.info("director: " + searchMovieRequest.getDirector());
                ServiceLogger.LOGGER.info("num: " + director);
                ps.setString(director, searchMovieRequest.getDirector());
            }
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query has succeeded.");
            ArrayList<MovieModel> list = new ArrayList<>();
            while (rs.next()) {
                MovieModel m = new MovieModel(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        rs.getFloat("rating"),
                        rs.getInt("numVotes"),
                        rs.getBoolean("hidden"),
                        null,
                        null
                );
                if (!m.getHidden())
                    m.setHidden(null);
                ServiceLogger.LOGGER.info("Retrieved movie " + m.toString());
                list.add(m);
            }
            MovieModel[] movies = list.toArray(new MovieModel[list.size()]);
            if (movies.length != 0)
                return new SearchMovieResponse(210, movies);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to query movie.");
            e.printStackTrace();
        }
        return new SearchMovieResponse(211, null);
    }

    public static GenreModel[] findGenres(String id) {
        ServiceLogger.LOGGER.info("Searching for genres fitting specific movie...");
        String query = "SELECT id, name FROM genres, genres_in_movies WHERE " +
                "genres_in_movies.genreId = genres.id AND movieId LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            ArrayList<GenreModel> list = new ArrayList<>();
            while (rs.next()) {
                GenreModel g = new GenreModel(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                ServiceLogger.LOGGER.info("Retrieved genre " + g);
                list.add(g);
            }
            GenreModel[] genres = list.toArray(new GenreModel[list.size()]);
            if (genres.length != 0)
                return genres;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to find genres.");
            e.printStackTrace();
        }
        return null;
    }

    public static StarModel[] findStars(String id) {
        ServiceLogger.LOGGER.info("Searching for stars acting in movie of movie id...");
        String query = "SELECT id, name, birthYear FROM stars, stars_in_movies WHERE stars.id = stars_in_movies.starId AND movieId LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            ArrayList<StarModel> list = new ArrayList<>();
            while (rs.next()) {
                StarModel s = new StarModel(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("birthYear"),
                        null
                );
                ServiceLogger.LOGGER.info("Retrieved genre " + s);
                list.add(s);
            }
            StarModel[] stars = list.toArray(new StarModel[list.size()]);
            if (stars.length != 0)
                return stars;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to find genres.");
            e.printStackTrace();
        }
        return null;
    }


    public static GetMovieIdResponse movieIdSearchQuery(String id, boolean privilege, VerifyPrivilegeResponse verifyPrivilegeResponse) {
        ServiceLogger.LOGGER.info("Searching for movies with id " + id);
        String query = "SELECT id, title, year, director, backdrop_path, budget, overview," +
                " poster_path, revenue, hidden, rating, numVotes" +
                " FROM movies, ratings WHERE movies.id = ratings.movieId AND id LIKE ?";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            MovieModel m = null;
            Boolean hidden = false;
            if (rs.next()) {
                m = new MovieModel(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        rs.getString("backdrop_path"),
                        rs.getInt("budget"),
                        rs.getString("overview"),
                        rs.getString("poster_path"),
                        rs.getInt("revenue"),
                        rs.getFloat("rating"),
                        rs.getInt("numVotes"),
                        null,
                        findGenres(rs.getString("id")),
                        findStars(rs.getString("id"))
                );
                ServiceLogger.LOGGER.info("Retrieved movie " + m);
                hidden = rs.getBoolean("hidden");
            }
            if (m == null) {
                return new GetMovieIdResponse(211, null, null);
            } else {
                if (hidden) {
                    if (privilege)
                        return new GetMovieIdResponse(210, m, null);
                    return new GetMovieIdResponse(141, null, verifyPrivilegeResponse);
                }
                return new GetMovieIdResponse(210, m, null);
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movies.");
            e.printStackTrace();
        }
        return new GetMovieIdResponse(211, null, null);
    }

    public static boolean movieAlreadyExist(String title) {
        ServiceLogger.LOGGER.info("Checking if movie title already exists.");
        String query = "SELECT id FROM movies WHERE title LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, title);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found existing movie.");
                return true;
            }
            ServiceLogger.LOGGER.info("Did not find movie.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addNewRating(String movieId) {
        String query = "INSERT INTO ratings (movieId, rating, numVotes) VALUES (?, ?, ?);";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ps.setFloat(2, 0);
            ps.setInt(3, 0);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to update ratings.");
            e.printStackTrace();
        }
        return false;
    }

    public static int findGenreId(String name) {
        String query = "SELECT id FROM genres WHERE name LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            ServiceLogger.LOGGER.info("Query succeeded.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to update ratings.");
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean addGenres(String name) {
        ServiceLogger.LOGGER.info("adding new genre into genres.");
        String query = "INSERT INTO genres (name) VALUES (?);";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.setString(1, name);
            ps.execute();
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check for top genre.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addGenreMovies(int genreId, String movieId) {
        ServiceLogger.LOGGER.info("Adding new genre into genres_in_movies.");
        String query = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?);";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setInt(1, genreId);
            ps.setString(2, movieId);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Success genre_in_movies insert");
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Could not add genre");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.warning("Could not add genre");
        return false;
    }

    public static String getCurrentCs() {
        ServiceLogger.LOGGER.info("Returning current cs% movie id.");
        String query = "SELECT id FROM movies WHERE id LIKE 'cs%' ORDER BY id desc;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Retrieved current cs string.");
                return rs.getString("id");
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: could not query.");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("No cs movie id returning first.");
        return "first";
    }

    public static boolean genreAlreadyExists(String name) {
        ServiceLogger.LOGGER.info("Checking if genre  exists.");
        String query = "SELECT * FROM genres WHERE name LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Genre exists.");
                return true;
            }
            ServiceLogger.LOGGER.info("Did not find genre.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static AddMovieResponse addNewMovie(AddMovieRequest requestModel) {
        if (movieAlreadyExist(requestModel.getTitle())) {
            ServiceLogger.LOGGER.info("Movie requested to be inserted already exists");
            return new AddMovieResponse(216, null, null);
        }
        ServiceLogger.LOGGER.info("Movie requested to be inserted does not exist...");
        ServiceLogger.LOGGER.info("Valid request continuing...");
        String lastMovieId = MovieRecords.getCurrentCs();
        int cmiNumber;
        String cminString;
        String currentMovieId = "cs";
        ServiceLogger.LOGGER.info("Processing ...");
        if (lastMovieId.equals("first")) {
            currentMovieId = "cs0000001";
        } else {
            // cmi = current movie Id number i.e. csxxxxxxx
            cmiNumber = Integer.parseInt(lastMovieId.substring(lastMovieId.length() - 7));
            cmiNumber++;
            cminString = Integer.toString(cmiNumber);
            int i = 0;
            for (i = 7 - cminString.length(); i > 0; i--) {
                currentMovieId += "0";
            }
            currentMovieId += cminString;
        }
        ServiceLogger.LOGGER.info("movieId for this movie will be" + currentMovieId);
        String query = "INSERT INTO movies (id, title, year, director, backdrop_path, budget, overview, poster_path, revenue)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, currentMovieId);
            ps.setString(2, requestModel.getTitle());
            ps.setInt(3, requestModel.getYear());
            ps.setString(4, requestModel.getDirector());

            if (requestModel.getBackdrop_path() != null)
                ps.setString(5, requestModel.getBackdrop_path());
            else
                ps.setString(5, null);

            if (requestModel.getBudget() != null)
                ps.setInt(6, requestModel.getBudget());
            else
                ps.setNull(6, Types.INTEGER);

            if (requestModel.getOverview() != null)
                ps.setString(7, requestModel.getOverview());
            else
                ps.setString(7, null);

            if (requestModel.getPoster_path() != null)
                ps.setString(8, requestModel.getPoster_path());
            else
                ps.setString(8, null);

            if (requestModel.getRevenue() != null)
                ps.setInt(9, requestModel.getRevenue());
            else
                ps.setNull(9, Types.INTEGER);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Insert new movie into database successfully.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed to insert movie");
            e.printStackTrace();
            return new AddMovieResponse(215, null, null);
        }
        ServiceLogger.LOGGER.info("Starting genres portion of adding...");
        GenreModel[] genres = requestModel.getGenres();
        List<Integer> list = new ArrayList<>();
        ServiceLogger.LOGGER.info("" + genres.length);
        int i = 0;
        while (i < genres.length) {
            if (!genreAlreadyExists(genres[i].getName())) {
                addGenres(genres[i].getName());
                ServiceLogger.LOGGER.info("added genre " + genres[i].getName());
            }
            int genreid = findGenreId(genres[i].getName());
            ServiceLogger.LOGGER.info(genreid + "");
            ServiceLogger.LOGGER.info(currentMovieId);
            addGenreMovies(genreid, currentMovieId);
            list.add(genreid);
            i++;
        }
        addNewRating(currentMovieId);
        int[] genre = list.stream().mapToInt(Integer::intValue).toArray();
        return new AddMovieResponse(214, currentMovieId, genre);
    }

    public static boolean removeMovie(String movieid) {
        ServiceLogger.LOGGER.info("Removing specified movie id");
        String query = "UPDATE movies SET hidden = 1 WHERE id LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.setString(1, movieid);
            ps.execute();
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean canRemove(String movieid) {
        ServiceLogger.LOGGER.info("Checking if movie title already exists.");
        String query = "SELECT id FROM movies WHERE id LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Movie id exist.");
                return true;
            }
            ServiceLogger.LOGGER.info("Did not find movie.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean alreadyRemoved(String movieid) {
        ServiceLogger.LOGGER.info("Checking if movie is already removed.");
        String query = "SELECT hidden FROM movies WHERE id LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("hidden"))
                    return true;
            }
            ServiceLogger.LOGGER.info("Did not find movie.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static GenreModel[] getGenreList() {
        String query = "SELECT id, name FROM genres;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<GenreModel> temp = new ArrayList<>();
            while (rs.next()) {
                GenreModel g = new GenreModel(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                temp.add(g);
            }
            GenreModel[] genres = temp.toArray(new GenreModel[temp.size()]);
            if (genres.length != 0)
                return genres;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addGenre(String name) {
        ServiceLogger.LOGGER.info("Inserting genre: " + name);
        String query = "INSERT INTO genres (name) VALUES (?);";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);
            ps.execute();
            ServiceLogger.LOGGER.info("Successful insert.");
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean genreExists(String name) {
        String query = "SELECT name FROM genres WHERE name = ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static GenreModel[] getMovieGenreList(String movieid) {
        String query = "SELECT name, genreId FROM genres, movies, genres_in_movies WHERE " +
                "movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = genres.id AND" +
                " movies.id = ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);
            ResultSet rs = ps.executeQuery();
            ArrayList<GenreModel> list = new ArrayList<>();
            while (rs.next()) {
                GenreModel g = new GenreModel(
                        rs.getInt("genreId"),
                        rs.getString("name")
                );
                ServiceLogger.LOGGER.info("Retrieved genre " + g);
                list.add(g);
            }
            GenreModel[] genres = list.toArray(new GenreModel[list.size()]);
            if (genres.length != 0)
                return genres;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean movieAlreadyExistId(String movieid) {
        ServiceLogger.LOGGER.info("Checking if movie title already exists.");
        String query = "SELECT id FROM movies WHERE id = ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found existing movie.");
                return true;
            }
            ServiceLogger.LOGGER.info("Did not find movie.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed");
            e.printStackTrace();
        }
        return false;
    }

    public static SearchStarResponse searchStars(SearchStarRequest requestModel) {
        ServiceLogger.LOGGER.info("Searching for stars with the specified information...");
        int paramIndex = 1;
        int n = 0, b = 0, m = 0;
        String query = "SELECT DISTINCT stars.id, stars.name, stars.birthYear FROM stars, movies, stars_in_movies WHERE" +
                " stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id";

        ServiceLogger.LOGGER.info("name: " + requestModel.getName());
        ServiceLogger.LOGGER.info("birthyear: " + requestModel.getBirthYear());
        ServiceLogger.LOGGER.info(requestModel.getMovieTitle());
        if(requestModel.getName() != null) {
            query += " AND name LIKE ? ";
            n = paramIndex;
            paramIndex++;
        }
        if(requestModel.getBirthYear() != null) {
            query += " AND birthYear LIKE ?";
            b = paramIndex;
            paramIndex++;
        }
        if(requestModel.getMovieTitle() != null) {
            query += " AND title LIKE ?";
            m = paramIndex;
            paramIndex++;
        }

        query += " ORDER BY " + requestModel.getOrderby() + " " + requestModel.getDirection() + ", birthYear asc " +
                "LIMIT " + requestModel.getLimit() + " OFFSET " + requestModel.getOffset() + ";";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            if (n != 0) {
                ps.setString(n, requestModel.getName());
            }
            if (b != 0) {
                ps.setInt(b, requestModel.getBirthYear());
            }
            if (m != 0) {
                ps.setString(m, requestModel.getMovieTitle());
            }
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            ArrayList<StarModel> list = new ArrayList<>();
            while (rs.next()) {
                StarModel s = new StarModel(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("birthYear"),
                        null
                );
                list.add(s);
            }
            StarModel[] stars = list.toArray(new StarModel[list.size()]);
            if (stars.length != 0) {
                return new SearchStarResponse(212, stars);
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve stars.");
            e.printStackTrace();
        }
        return new SearchStarResponse(213, null);
    }
    public static GetStarIdResponse searchStarId(String id){
        ServiceLogger.LOGGER.info("Checking star associated with star id.");
        String query = "SELECT id, name, birthYear FROM stars WHERE id = ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found star.");
                StarModel s = new StarModel(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("birthYear"),
                        null
                );
                return new GetStarIdResponse(212, s);
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed stars table");
            e.printStackTrace();
        }
        return new GetStarIdResponse(213, null);
    }
    public static boolean starExists(AddStarRequest addStarRequest){
        int b = 0;
        String query = "SELECT name FROM stars WHERE name = ?";
        if(addStarRequest.getBirthYear() != null){
            query +=  " AND birthYear = ?;";
            b++;
        }
        query += ";";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, addStarRequest.getName());
            if(b != 0){
                ps.setInt(2, addStarRequest.getBirthYear());
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }

    public static AddStarResponse addNewStar(AddStarRequest addStarRequest){
        ServiceLogger.LOGGER.info("Inserting star: " + addStarRequest.getName());
        String query = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?);";
        try {
            String lastStarId = MovieRecords.getCurrentSS();
            int cmNumber;
            String cminString;
            String currentStarId = "ss";
            ServiceLogger.LOGGER.info("Processing ...");
            if (lastStarId.equals("first")) {
                currentStarId = "ss0000001";
            } else {
                int i = 0;
                cmNumber = Integer.parseInt(lastStarId.substring(lastStarId.length() - 7));
                cmNumber++;
                cminString = Integer.toString(cmNumber);
                for (i = 7 - cminString.length(); i > 0; i--) {
                    currentStarId += "0";
                }
                currentStarId += cminString;
            }
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, currentStarId);
            ps.setString(2, addStarRequest.getName());
            ps.setInt(3, addStarRequest.getBirthYear());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Successful insert.");
            return new AddStarResponse(220);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return new AddStarResponse(221);
    }

    public static String getCurrentSS() {
        ServiceLogger.LOGGER.info("Returning current SS star id.");
        String query = "SELECT id FROM stars WHERE id LIKE 'ss%' ORDER BY id desc;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Got current SS.");
                return rs.getString("id");
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: could not query.");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("No ss star id returning first.");
        return "first";
    }

    public static boolean movieIdAlreadyExist(String id) {
        ServiceLogger.LOGGER.info("Checking if movie title already exists.");
        String query = "SELECT id FROM movies WHERE id LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found existing movie from movie id.");
                return true;
            }
            ServiceLogger.LOGGER.info("Did not find movie.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return false;
    }
    public static boolean starsInAlreadyExist(String starid, String movieid) {
        ServiceLogger.LOGGER.info("Checking if pair already exists.");
        String query = "SELECT * FROM stars_in_movies WHERE starId = ? AND movieId = ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starid);
            ps.setString(2, movieid);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found existing pair.");
                return true;
            }
            ServiceLogger.LOGGER.info("Did not find pair.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed");
            e.printStackTrace();
        }
        return false;
    }
    public static StarsInResponse addStarsIn(StarsInRequest starsInRequest) {
        ServiceLogger.LOGGER.info("Inserting star in movies");
        String query = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?);";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starsInRequest.getStarid());
            ps.setString(2, starsInRequest.getMovieid());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Successful insert.");
            return new StarsInResponse(230);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return new StarsInResponse(231);
    }
    public static int currentVotes(RatingRequest ratingRequest){
        ServiceLogger.LOGGER.info("Getting current ratins info");
        String query = "SELECT numVotes FROM ratings WHERE movieId = ?;";
        int numVotes = 0;
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, ratingRequest.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found existing pair.");
                numVotes = rs.getInt("numVotes");
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return numVotes;
    }

    public static RatingResponse addRating(RatingRequest ratingRequest){
        int numVotes = currentVotes(ratingRequest);
        numVotes++;
        String query = "UPDATE ratings SET rating = ?, numVotes = ? WHERE movieId LIKE ?;";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setFloat(1, ratingRequest.getRating());
            ps.setInt(2, numVotes);
            ps.setString(3, ratingRequest.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            return new RatingResponse(250);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: check if movies exist.");
            e.printStackTrace();
        }
        return new RatingResponse(251);

    }

}
