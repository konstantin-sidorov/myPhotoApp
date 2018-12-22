package myPhotoApp.db;


import java.sql.*;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execSimpleQuery(String query) throws DataBaseException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
        } catch (SQLException e) {
            DataBaseException se = new DataBaseException("execSimpleQuery error");
            se.initCause(e);
            throw se;
        }
    }

    public void execUpdate(String update, ExecuteHandler prepare) throws DataBaseException {
        try {
            PreparedStatement stmt = connection.prepareStatement(update);
            prepare.accept(stmt);
            stmt.close();
        } catch (SQLException e) {
            DataBaseException se = new DataBaseException("execUpdate error");
            se.initCause(e);
            throw se;
        }

    }

    public <T> T execQuery(String query, TResultHandler<T> handler) throws DataBaseException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        } catch (SQLException e) {
            DataBaseException se = new DataBaseException("execQuery error");
            se.initCause(e);
            throw se;
        }
    }

    @FunctionalInterface
    public interface ExecuteHandler {
        void accept(PreparedStatement statement) throws DataBaseException;
    }

    @FunctionalInterface
    public interface TResultHandler<T> {
        T handle(ResultSet resultSet) throws DataBaseException, SQLException;
    }

}