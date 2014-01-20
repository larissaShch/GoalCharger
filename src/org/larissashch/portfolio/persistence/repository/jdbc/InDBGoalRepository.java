package org.larissashch.portfolio.persistence.repository.jdbc;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.larissashch.portfolio.goalcharger.model.entity.Administrator;
import org.larissashch.portfolio.goalcharger.model.entity.BaseEntity;
import org.larissashch.portfolio.goalcharger.model.entity.CategoryType;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.Goal;
import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.goalcharger.model.entity.StatusType;
import org.larissashch.portfolio.persistence.repository.GoalRepository;
import org.larissashch.portfolio.goalcharger.model.entity.Step;

public class InDBGoalRepository implements GoalRepository {

	protected String dbName;
	protected String tableNameGoal;
	protected String tableNameStep;

	protected String connectionUrl;
	private ReusableConnectionPool connectionPool;

	public InDBGoalRepository(boolean testFlag) {
		this.dbName = "DerbyDBGoalCharger";
		this.tableNameGoal = "Goal";
		this.tableNameStep = "Step";

		if (testFlag) {
			dbName = "Test" + dbName;
		}

		connectionUrl = "jdbc:derby:" + dbName + ";create=true";
		connectionPool = new ReusableConnectionPool(connectionUrl, 50);

		if (!this.isDBExist()) {
			this.createNewDB();
		}

	}

	private boolean isDBExist() {
		File file = new File(dbName);
		if (file.isDirectory()) {
			return true;
		}
		return false;
	}

	private void createNewDB() {

		try {
			ReusableConnection connection = connectionPool.getConnection();
			try (Statement statement = connection.createStatement()) {
				statement
						.executeUpdate("CREATE TABLE Goal("
								+ "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
								+ "createdDate DATE NOT NULL,"
								+ "createdByUserId integer,"
								+ "createdByUserType VARCHAR(100),"
								+ "name VARCHAR(255),"
								+ "description VARCHAR(750),"
								+ "category VARCHAR(100),"
								+ "status VARCHAR(100)," 
								+ "startDate DATE,"
								+ "targetDate DATE," 
								+ "percentOfCharge FLOAT"
								+ ")");
				System.out.println("Table 'Goal' was created;");

				statement
						.executeUpdate("CREATE TABLE Step("
								+ "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
								+ "createdDate DATE NOT NULL,"
								+ "createdByUserId integer,"
								+ "createdByUserType VARCHAR(100),"
								+ "name VARCHAR(255),"
								+ "description VARCHAR(750),"
								+ "status VARCHAR(100)," 
								+ "startDate DATE,"
								+ "targetDate DATE,"
								+ ")");

				System.out.println("Table 'Step' was created;");

				statement.executeUpdate("CREATE TABLE KeyWords("
						+ "id integer," 
						+ "keyWord VARCHAR(255)" 
						+ ")");
				System.out.println("Table 'KeyWords' was created;");

				statement.executeUpdate("CREATE TABLE KeyWordsGoal("
						+ "keywordId integer," 
						+ "goalId integer"
						+ ")");
				System.out.println("Table 'KeyWordsGoal' was created;");

				statement.executeUpdate("CREATE TABLE KeyWordsStep("
						+ "keywordId integer," 
						+ "stepId integer"

						+ ")");
				System.out.println("Table 'KeyWordsStep' was created;");

				statement.executeUpdate("CREATE TABLE GoalSteps("
						+ "goalId integer," + "stepId integer" + ")");
				System.out.println("Table 'GoalSteps' was created;");

				connectionPool.releaseConnection(connection);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private Date stringToDate(String dateString) {
		String messageException = "Error Message: \nMethod: stringToDate(String dateString); \ndateString: "
				+ dateString;

		if (dateString.length() > 0) {
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("dd.MM.yyyy");
			Date date;
			try {
				date = format.parse(dateString);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(messageException);
			}

		}

		return null;
	}

	private Date nullToDate(Date date) {
		if (date == null) {
			return this.stringToDate("01.01.0001");
		}
		return date;
	}

	private Date dateToNull(Date date) {
		if (date == null || date.equals(this.stringToDate("01.01.0001"))) {
			return null;
		}

		return date;
	}

	private String objectToString(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	private void valueToSatement(PreparedStatement statement, String fieldType,
			Object value, int index) throws SQLException {
		if (fieldType.equals("Int")) {
			statement.setInt(index, (Integer) value);
		}
		if (fieldType.equals("Long")) {
			statement.setLong(index, (Long) value);

		}
		if (fieldType.equals("String")) {
			statement.setString(index, (String) value);
		}

		if (fieldType.equals("Date")) {
			statement.setDate(index, (java.sql.Date) value);
		}
		if (fieldType.equals("Float")) {
			statement.setFloat(index, (Float) value);
		}

	}

	private int save(String tableName, List<String> fieldsNames,
			List<String> fieldsTypes, List<Object> filedsValues) {
		int lastId = 0;
		ReusableConnection connection = connectionPool.getConnection();
		String substitution = "";
		String str = "INSERT INTO " + tableName + "(";
		for (int i = 0; i < fieldsNames.size(); i++) {
			str = str + fieldsNames.get(i);
			substitution = substitution + "?";
			if (i < fieldsNames.size() - 1) {
				str = str + ", ";
				substitution = substitution + ", ";
			}
		}

		str = str + ")VALUES(";

		str = str + substitution;

		str = str + ")";

		System.out.println(str);

		try (PreparedStatement statement = connection.prepareStatement(str)) {
			for (int i = 0; i < filedsValues.size(); i++) {
				this.valueToSatement(statement, fieldsTypes.get(i),
						filedsValues.get(i), i + 1);

			}

			statement.executeUpdate();
			try (Statement statement2 = connection.createStatement()) {
				str = "select max(id) from " + tableName;

				try (ResultSet rs = statement2.executeQuery(str)) {

					while (rs.next()) {
						lastId = rs.getInt(1);
						System.out.println("New record with id:" + lastId);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("");
		} finally {
			connectionPool.releaseConnection(connection);
		}
		return lastId;
	}

	private void update(String tableName, int id, List<String> fieldsNames,
			List<String> fieldsTypes, List<Object> filedsValues) {
		ReusableConnection connection = connectionPool.getConnection();

		String str = "UPDATE " + tableName + " SET ";
		for (int i = 0; i < fieldsNames.size(); i++) {
			str = str + fieldsNames.get(i) + " = ?";

			if (i < fieldsNames.size() - 1) {
				str = str + ", ";

			}
		}

		str = str + " WHERE id=" + id;

		System.out.println(str);

		try (PreparedStatement statement = connection.prepareStatement(str)) {
			for (int i = 0; i < filedsValues.size(); i++) {
				this.valueToSatement(statement, fieldsTypes.get(i),
						filedsValues.get(i), i + 1);
			}

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("");
		} finally {
			connectionPool.releaseConnection(connection);
		}
	}

	private void delete(String tableName, int id) {
		String str = "DELETE FROM " + tableName + " WHERE id = ?";
		ReusableConnection connection = connectionPool.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error in method delete(String tableName, long id), id:"
							+ id + ", tableName:" + tableName);
		} finally {
			connectionPool.releaseConnection(connection);
		}
	}



	private <T extends BaseEntity> void updateList(List<T> listOfValues,
			int objectId, String tableName, String objectIdFieldName,
			String valueFieldName) {
		if (listOfValues != null) {
			int quantity = 0;
			for (T value : listOfValues) {
				ReusableConnection connection = connectionPool.getConnection();
				try (Statement statement = connection.createStatement()) {
					String str = "select count(*) from " + tableName
							+ " where " + objectIdFieldName + "=" + objectId
							+ " and " + valueFieldName + "=" + value.getId();

					try (ResultSet rs = statement.executeQuery(str)) {

						while (rs.next()) {
							quantity = rs.getInt(1);

						}

						if (quantity == 0) {
							str = "INSERT INTO " + tableName + " ("
									+ objectIdFieldName + ", " + valueFieldName
									+ ") VALUES (" + objectId + ", "
									+ value.getId() + ")";
							try (Statement statement2 = connection
									.createStatement()) {
								statement2.executeUpdate(str);
							}

						}

					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("");
				} finally {
					connectionPool.releaseConnection(connection);
				}
			}
		}
	}

	private List<Integer> getListOfId(String tableName, String fieldName,
			String objectFieldName, Integer objectId) {
		List<Integer> list = new ArrayList<>();

		ReusableConnection connection = connectionPool.getConnection();
		try (Statement statement = connection.createStatement()) {
			String str = "select " + fieldName + " from " + tableName
					+ " where " + objectFieldName + "=" + objectId;

			try (ResultSet rs = statement.executeQuery(str)) {

				while (rs.next()) {
					list.add(rs.getInt(fieldName));

				}
				return list;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("");
		} finally {
			connectionPool.releaseConnection(connection);
		}

	}

	@Override
	public void saveGoal(Goal goal) {
		System.out.println("Started method saveGoal(goal).\n");
		long createdByUserId;
		String createdByUserType;

		if (goal.getCreatedBy() == null) {
			createdByUserId = 0;
			createdByUserType = "Administrator";
		} else {
			createdByUserId = goal.getCreatedBy().getId();
			createdByUserType = goal.getCreatedBy().getUserType();
		}

		List<String> fieldsNames = new ArrayList<>();
		fieldsNames.add("createdDate");
		fieldsNames.add("createdByUserId");
		fieldsNames.add("createdByUserType");
		fieldsNames.add("name");
		fieldsNames.add("description");

		fieldsNames.add("category");
		fieldsNames.add("status");
		fieldsNames.add("startDate");
		fieldsNames.add("targetDate");
		fieldsNames.add("percentOfCharge");

		List<String> fieldsTypes = new ArrayList<>();
		fieldsTypes.add("Date");
		fieldsTypes.add("Int");
		fieldsTypes.add("String");
		fieldsTypes.add("String");
		fieldsTypes.add("String");

		fieldsTypes.add("String");
		fieldsTypes.add("String");
		fieldsTypes.add("Date");
		fieldsTypes.add("Date");
		fieldsTypes.add("Float");

		List<Object> filedsValues = new ArrayList<>();
		filedsValues.add(new java.sql.Date(this
				.nullToDate(goal.getCreateDate()).getTime()));
		filedsValues.add(createdByUserId);
		filedsValues.add(createdByUserType);
		filedsValues.add(goal.getGoalName());
		filedsValues.add(goal.getDescription());

		filedsValues.add(this.objectToString(goal.getCategory()));
		filedsValues.add(this.objectToString(goal.getStatus()));

		filedsValues.add(new java.sql.Date(this.nullToDate(goal.getStartDate())
				.getTime()));
		filedsValues.add(new java.sql.Date(this
				.nullToDate(goal.getTargetDate()).getTime()));

		filedsValues.add(goal.getPercentOfCharge());

		if (goal.getId() > 0) {

			this.update("Goal", goal.getId(), fieldsNames, fieldsTypes,
					filedsValues);
		} else {

			goal.setId(this
					.save("Goal", fieldsNames, fieldsTypes, filedsValues));
		}

		
		this.updateList(goal.getKeyWords(), goal.getId(), "KeyWordsGoal", "keyWordlId",
				"goalId");

		this.updateList(goal.getSteps(), goal.getId(), "GoalSteps", "goalId",
				"stepId");

		System.out.println("\nFinished method saveGoal(goal). id:"
				+ goal.getId() + " \n\n\n");

	}

	@Override
	public Goal readGoal(int id) {
		System.out.println("Started method readGoal(id).\n");
		if (id > 0) {

			ReusableConnection connection = connectionPool.getConnection();
			try (Statement statement = connection.createStatement()) {
				String str = "select * from Goal where id=" + id + "";
				try (ResultSet rs = statement.executeQuery(str)) {

					while (rs.next()) {
						Goal goal = new Goal();
						goal.setId(rs.getInt(rs.findColumn("id")));
						goal.setCreateDate(this.dateToNull(rs.getDate(rs
								.findColumn("createdDate"))));
						
						goal.setGoalName(rs.getString(rs.findColumn("name")));
						goal.setGoalName(rs.getString(rs.findColumn("description")));

						
						

						goal.setCategory(CategoryType.getCategoryType(rs.getString(rs.findColumn("category"))));
						goal.setStatus(StatusType.getStatusType(rs.getString(rs.findColumn("status"))));
						
						goal.setStartDate(this.dateToNull(rs.getDate(rs.findColumn("startDate"))));
						goal.setTargetDate(this.dateToNull(rs.getDate(rs.findColumn("targetDate"))));
						
						goal.setPercentOfCharge(rs.getFloat(rs.findColumn("percentOfCharge")));
						
						
						List<Integer> keyWordsIdList = this.getListOfId("KeyWordsGoal", "keyWordId", "goalId", goal.getId());
						List<KeyWord> keyWords = new ArrayList<>();
						for(Integer keyWordId:keyWordsIdList){
							keyWords.add(this.readKeyWord(keyWordId));
						}
						
						goal.setKeyWords(keyWords);
						
						List<Integer> stepsIdList = this.getListOfId("StepsGoal", "stepId", "goalId", goal.getId());
						List<Step> steps = new ArrayList<>();
						for(Integer stepId:stepsIdList){
							steps.add(this.readStep(stepId));
						}
						goal.setSteps(steps);
						
						

						int createdById = rs.getInt(rs.findColumn("id"));
						String createdByUserType = rs.getString(rs
								.findColumn("createdByUserType"));

						if (createdById == 0) {
							Administrator administrator = new Administrator();
							administrator.setId(0);
							goal.setCreatedBy(administrator);
						} else {
							if (createdByUserType.equals("Customer")) {
								Customer createdByCustomer = new Customer();
								createdByCustomer.setId(createdById);
								goal.setCreatedBy(createdByCustomer);
							}
							if (createdByUserType.equals("Administrator")) {
								Administrator administrator = new Administrator();
								administrator.setId(createdById);
								goal.setCreatedBy(administrator);
							}
							System.out
									.println("\nFinished method readMoneyAccount(id). id:"
											+ id + " \n\n\n");
							return goal;
						}

					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Error in method readMoneyAccount(long id), id:" + id);
			} finally {
				connectionPool.releaseConnection(connection);
			}
		}
		System.out.println("\nFinished method readMoneyAccount(id). id:" + id
				+ " \n\n\n");
		return null;
	}

	@Override
	public void deleteGoal(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveStep(Step step) {
		// TODO Auto-generated method stub

	}

	@Override
	public Step readStep(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteStep(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveKeyWord(KeyWord keyWord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KeyWord readKeyWord(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int readKeyWord(String keyWord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteKeyWord(int id) {
		// TODO Auto-generated method stub
		
	}

}
