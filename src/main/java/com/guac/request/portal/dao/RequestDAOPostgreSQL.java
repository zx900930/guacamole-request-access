package com.guac.request.portal.dao;

import com.guac.request.portal.model.AccessRequest;
import com.guac.request.portal.model.TargetEnvironment;
import org.apache.guacamole.GuacamoleException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.guac.request.portal.util.GuacamoleProperties;

public class RequestDAOPostgreSQL implements RequestDAO {

    private Connection getConnection() throws SQLException, GuacamoleException {
        Properties properties = GuacamoleProperties.getGuacamoleProperties();
        String url = properties.getProperty("postgresql-jdbc-url");
        String username = properties.getProperty("postgresql-username");
        String password = properties.getProperty("postgresql-password");
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void submitAccessRequest(AccessRequest request) throws GuacamoleException {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO guacamole_access_request (request_id, username, target_environment, reason_for_request, estimated_time, request_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, request.getRequestId());
            statement.setString(2, request.getUsername());
            statement.setString(3, request.getTargetEnvironment());
            statement.setString(4, request.getReasonForRequest());
            statement.setInt(5, request.getEstimatedTime());
            statement.setDate(6, new java.sql.Date(request.getRequestDate().getTime()));
            statement.setString(7, request.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GuacamoleException("Error submitting access request", e);
        }
    }

    @Override
    public List<AccessRequest> getAllAccessRequests() throws GuacamoleException {
        List<AccessRequest> requests = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT request_id, username, target_environment, reason_for_request, estimated_time, request_date, status FROM guacamole_access_request";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AccessRequest request = new AccessRequest();
                request.setRequestId(resultSet.getString("request_id"));
                request.setUsername(resultSet.getString("username"));
                request.setTargetEnvironment(resultSet.getString("target_environment"));
                request.setReasonForRequest(resultSet.getString("reason_for_request"));
                request.setEstimatedTime(resultSet.getInt("estimated_time"));
                request.setRequestDate(resultSet.getDate("request_date"));
                request.setStatus(resultSet.getString("status"));
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new GuacamoleException("Error retrieving access requests", e);
        }
        return requests;
    }

    @Override
    public void approveAccessRequest(String requestId) throws GuacamoleException {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE guacamole_access_request SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GuacamoleException("Error approving access request", e);
        }
    }

    @Override
    public void denyAccessRequest(String requestId) throws GuacamoleException {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE guacamole_access_request SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GuacamoleException("Error denying access request", e);
        }
    }

    @Override
    public void addTargetEnvironment(TargetEnvironment environment) throws GuacamoleException {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO guacamole_target_environment (environment_id, name, description) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, environment.getEnvironmentId());
            statement.setString(2, environment.getName());
            statement.setString(3, environment.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GuacamoleException("Error adding target environment", e);
        }
    }

    @Override
    public List<TargetEnvironment> getAllTargetEnvironments() throws GuacamoleException {
        List<TargetEnvironment> environments = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT environment_id, name, description FROM guacamole_target_environment";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TargetEnvironment environment = new TargetEnvironment();
                environment.setEnvironmentId(resultSet.getString("environment_id"));
                environment.setName(resultSet.getString("name"));
                environment.setDescription(resultSet.getString("description"));
                environments.add(environment);
            }
        } catch (SQLException e) {
            throw new GuacamoleException("Error retrieving target environments", e);
        }
        return environments;
    }

    @Override
    public void updateTargetEnvironment(TargetEnvironment environment) throws GuacamoleException {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE guacamole_target_environment SET name = ?, description = ? WHERE environment_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, environment.getName());
            statement.setString(2, environment.getDescription());
            statement.setString(3, environment.getEnvironmentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GuacamoleException("Error updating target environment", e);
        }
    }

    @Override
    public void deleteTargetEnvironment(String environmentId) throws GuacamoleException {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM guacamole_target_environment WHERE environment_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, environmentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GuacamoleException("Error deleting target environment", e);
        }
    }
}