package repository.db;

import domain.Friendship;
import domain.User;
import domain.validators.Validator;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendshipsDbRepository implements Repository<Long, Friendship> {

    private final String url;
    private final String username;
    private final String password;

    private final Validator<Friendship> validator;

    public FriendshipsDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;

    }


    @Override
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "insert into friendships ( friend_one_id, friend_two_id) values (?, ?)";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, entity.getId1());
            preparedStatement.setLong(2, entity.getId2());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "delete from friendships where id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "select * from friendships where id = ? ";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long friendOneId = resultSet.getLong("friend_one_id");
            Long friendTwoId = resultSet.getLong("friend_two_id");

            return new Friendship(friendOneId, friendTwoId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }

    @Override
    public List<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long friendOneId = resultSet.getLong("friend_one_id");
                Long friendTwoId = resultSet.getLong("friend_two_id");
                Friendship friendship = new Friendship(friendOneId, friendTwoId);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }


    @Override
    public List<Friendship> getFriends(Friendship friendship) {
        return null;
    }

}