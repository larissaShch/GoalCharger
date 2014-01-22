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
								+ "status VARCHAR(100)," + "startDate DATE,"
								+ "targetDate DATE," + "percentOfCharge FLOAT"
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
								+ "status VARCHAR(100)," + "startDate DATE,"
								+ "targetDate DATE" 
								+ ")");

				System.out.println("Table 'Step' was created;");

				statement.executeUpdate("CREATE TABLE KeyWords("
						+ "id integer," + "keyWord VARCHAR(255)" + ")");
				System.out.println("Table 'KeyWords' was created;");

				statement.executeUpdate("CREATE TABLE KeyWordsGoal("
						+ "keywordId integer," + "goalId integer" + ")");
				System.out.println("Table 'KeyWordsGoal' was created;");

				statement.executeUpdate("CREATE TABLE KeyWordsStep("
						+ "keywordId integer," + "stepId integer"

						+ ")");
				System.out.println("Table 'KeyWordsStep' was created;");

				statement.executeUpdate("CREATE TABLE GoalSteps("
						+ "goalId integer," + "stepId integer" + ")");
				System.out.println("Table 'GoalSteps' was created;");
				
				System.out.println("DB created. Release connection.");
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

		//System.out.println(str);

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
						//System.out.println("New record with id:" + lastId);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("");
		} finally {
			System.out.println("Save method for table:"+tableName+". Release connection.");
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

		//System.out.println(str);

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
			System.out.println("Updated method for table:"+tableName+". Release connection.");
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
			System.out.println("Delete method for table:"+tableName+". Release connection.");
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
					System.out.println("UpdatedList method for table:"+tableName+". Release connection.");
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
			System.out.println("getListOfId method for table:"+tableName+". Release connection.");
			connectionPool.releaseConnection(connection);
		}

	}

	private int getCount(String tableName) {
		ReusableConnection connection = connectionPool.getConnection();
		try (Statement statement = connection.createStatement()) {
			String str = "select count(id) from " + tableName;

			try (ResultSet rs = statement.executeQuery(str)) {

				while (rs.next()) {
					System.out.println("test"+rs.getInt(1));
					return rs.getInt(1);

				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("");
		} finally {
			System.out.println("GetCount method for table:"+tableName+". Release connection.");
			connectionPool.releaseConnection(connection);
		}

		return 0;
	}

	@Override
	public void saveGoal(Goal goal) {
		System.out.println("Started method saveGoal(goal).\n");
		int createdByUserId;
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

		this.updateList(goal.getKeyWords(), goal.getId(), "KeyWordsGoal",
				"keyWordId", "goalId");

		this.updateList(goal.getSteps(), goal.getId(), "GoalSteps", "stepId",
				"goalId");

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
						goal.setDescription(rs.getString(rs
								.findColumn("description")));

						goal.setCategory(CategoryType.getCategoryType(rs
								.getString(rs.findColumn("category"))));
						goal.setStatus(StatusType.getStatusType(rs.getString(rs
								.findColumn("status"))));

						goal.setStartDate(this.dateToNull(rs.getDate(rs
								.findColumn("startDate"))));
						goal.setTargetDate(this.dateToNull(rs.getDate(rs
								.findColumn("targetDate"))));

						goal.setPercentOfCharge(rs.getFloat(rs
								.findColumn("percentOfCharge")));

						List<Integer> keyWordsIdList = this.getListOfId(
								"KeyWordsGoal", "keyWordId", "goalId",
								goal.getId());
						List<KeyWord> keyWords = new ArrayList<>();
						for (Integer keyWordId : keyWordsIdList) {
							keyWords.add(this.readKeyWord(keyWordId));
						}

						goal.setKeyWords(keyWords);

						List<Integer> stepsIdList = this.getListOfId(
								"GoalSteps", "stepId", "goalId", goal.getId());
						List<Step> steps = new ArrayList<>();
						for (Integer stepId : stepsIdList) {
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
									.println("\nFinished method readGoal(id). id:"
											+ id + " \n\n\n");
							return goal;
						}

					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Error in method readGoal(int id), id:" + id);
			} finally {
				System.out.println("ReadGoal method. Release connection.");
				connectionPool.releaseConnection(connection);
			}
		}
		System.out.println("\nFinished method readGoal(id). id:" + id
				+ " \n\n\n");
		return null;
	}

	@Override
	public void deleteGoal(int id) {
		this.delete("Goal", id);

	}

	@Override
	public void saveStep(Step step) {
		System.out.println("Started method saveStep(step).\n");
		int createdByUserId;
		String createdByUserType;

		if (step.getCreatedBy() == null) {
			createdByUserId = 0;
			createdByUserType = "Administrator";
		} else {
			createdByUserId = step.getCreatedBy().getId();
			createdByUserType = step.getCreatedBy().getUserType();
		}

		List<String> fieldsNames = new ArrayList<>();
		fieldsNames.add("createdDate");
		fieldsNames.add("createdByUserId");
		fieldsNames.add("createdByUserType");
		fieldsNames.add("name");
		fieldsNames.add("description");

		fieldsNames.add("status");
		fieldsNames.add("startDate");
		fieldsNames.add("targetDate");

		List<String> fieldsTypes = new ArrayList<>();
		fieldsTypes.add("Date");
		fieldsTypes.add("Int");
		fieldsTypes.add("String");
		fieldsTypes.add("String");
		fieldsTypes.add("String");

		fieldsTypes.add("String");
		fieldsTypes.add("Date");
		fieldsTypes.add("Date");

		List<Object> filedsValues = new ArrayList<>();
		filedsValues.add(new java.sql.Date(this
				.nullToDate(step.getCreateDate()).getTime()));
		filedsValues.add(createdByUserId);
		filedsValues.add(createdByUserType);
		filedsValues.add(step.getName());
		filedsValues.add(step.getDescription());

		filedsValues.add(this.objectToString(step.getStatus()));

		filedsValues.add(new java.sql.Date(this.nullToDate(step.getStartDate())
				.getTime()));
		filedsValues.add(new java.sql.Date(this
				.nullToDate(step.getTargetDate()).getTime()));

		if (step.getId() > 0) {

			this.update("Step", step.getId(), fieldsNames, fieldsTypes,
					filedsValues);
		} else {

			step.setId(this
					.save("Step", fieldsNames, fieldsTypes, filedsValues));
		}

		this.updateList(step.getKeyWords(), step.getId(), "KeyWordsStep",
				"keyWordId", "stepId");

		System.out.println("\nFinished method saveStep(step). id:"
				+ step.getId() + " \n\n\n");

	}

	@Override
	public Step readStep(int id) {
		System.out.println("Started method readStep(id).\n");
		if (id > 0) {

			ReusableConnection connection = connectionPool.getConnection();
			try (Statement statement = connection.createStatement()) {
				String str = "select * from Step where id=" + id + "";
				try (ResultSet rs = statement.executeQuery(str)) {

					while (rs.next()) {
						Step step = new Step();
						step.setId(rs.getInt(rs.findColumn("id")));
						step.setCreateDate(this.dateToNull(rs.getDate(rs
								.findColumn("createdDate"))));

						step.setName(rs.getString(rs.findColumn("name")));
						step.setDescription(rs.getString(rs
								.findColumn("description")));

						step.setStatus(StatusType.getStatusType(rs.getString(rs
								.findColumn("status"))));

						step.setStartDate(this.dateToNull(rs.getDate(rs
								.findColumn("startDate"))));
						step.setTargetDate(this.dateToNull(rs.getDate(rs
								.findColumn("targetDate"))));

						List<Integer> keyWordsIdList = this.getListOfId(
								"KeyWordsStep", "keyWordId", "goalId",
								step.getId());
						List<KeyWord> keyWords = new ArrayList<>();
						for (Integer keyWordId : keyWordsIdList) {
							keyWords.add(this.readKeyWord(keyWordId));
						}

						step.setKeyWords(keyWords);

						int createdById = rs.getInt(rs.findColumn("id"));
						String createdByUserType = rs.getString(rs
								.findColumn("createdByUserType"));

						if (createdById == 0) {
							Administrator administrator = new Administrator();
							administrator.setId(0);
							step.setCreatedBy(administrator);
						} else {
							if (createdByUserType.equals("Customer")) {
								Customer createdByCustomer = new Customer();
								createdByCustomer.setId(createdById);
								step.setCreatedBy(createdByCustomer);
							}
							if (createdByUserType.equals("Administrator")) {
								Administrator administrator = new Administrator();
								administrator.setId(createdById);
								step.setCreatedBy(administrator);
							}
							System.out
									.println("\nFinished method readStep(id). id:"
											+ id + " \n\n\n");
							return step;
						}

					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Error in method readStep(int id), id:" + id);
			} finally {
				System.out.println("ReadStep method. Release connection.");
				connectionPool.releaseConnection(connection);
			}
		}
		System.out.println("\nFinished method readStep(id). id:" + id
				+ " \n\n\n");
		return null;
	}

	@Override
	public void deleteStep(int id) {
		this.delete("Step", id);

	}

	@Override
	public void saveKeyWord(KeyWord keyWord) {
		System.out.println("Started method saveKeyWord(keyWord).\n");
		int id = this.readKeyWord(keyWord.getValue());
		if (id == 0) {

			List<String> fieldsNames = new ArrayList<>();
			fieldsNames.add("value");

			List<String> fieldsTypes = new ArrayList<>();
			fieldsTypes.add("String");

			List<Object> filedsValues = new ArrayList<>();

			filedsValues.add(keyWord.getValue());

			this.save("KeyWords", fieldsNames, fieldsTypes, filedsValues);

		}

		System.out.println("\nFinished method saveKeyWord(keyWord). value:"
				+ keyWord.getValue() + " \n\n\n");

	}

	@Override
	public KeyWord readKeyWord(int id) {
		System.out.println("Started method readKeyWord(id).\n");
		if (id > 0) {

			ReusableConnection connection = connectionPool.getConnection();
			try (Statement statement = connection.createStatement()) {
				String str = "select * from KeyWords where id=" + id + "";
				try (ResultSet rs = statement.executeQuery(str)) {

					while (rs.next()) {
						KeyWord keyWord = new KeyWord();
						keyWord.setId(rs.getInt(rs.findColumn("id")));

						keyWord.setValue(rs.getString(rs.findColumn("value")));

						System.out
								.println("\nFinished method readKeyWord(id). id:"
										+ id + " \n\n\n");
						return keyWord;

					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Error in method readKeyWord(int id), id:" + id);
			} finally {
				System.out.println("ReadKeyWord method. Release connection.");
				connectionPool.releaseConnection(connection);
			}
		}

		System.out.println("\nFinished method readKeyWord(id). id:" + id
				+ " \n\n\n");
		return null;

	}

	@SuppressWarnings("unused")
	@Override
	public int readKeyWord(String keyWord) {
		System.out.println("Started method readKeyWord(KeyWord).\n");
		if (!keyWord.equals("") || keyWord != null) {

			ReusableConnection connection = connectionPool.getConnection();
			try (Statement statement = connection.createStatement()) {
				String str = "select * from KeyWords where value=" + keyWord
						+ "";
				try (ResultSet rs = statement.executeQuery(str)) {

					int id = 0;
					while (rs.next()) {
						id = rs.getInt(rs.findColumn("id"));

					}
					System.out
							.println("\nFinished method readKeyWord(String keyWord), keyWord:"
									+ keyWord + " \n\n\n");
					return id;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Error in method readKeyWord(String keyWord), keyWord:"
								+ keyWord);
			} finally {
				System.out.println("ReadKeyWord method. Release connection.");
				connectionPool.releaseConnection(connection);
			}
		}

		System.out
				.println("\nFinished method readKeyWord(String keyWord), keyWord:"
						+ keyWord + " \n\n\n");
		return 0;
	}

	@Override
	public void deleteKeyWord(int id) {
		this.delete("KeyWords", id);

	}

	@Override
	public int getGoalCount() {
		return this.getCount("Goal");
	}

	@Override
	public int getStepCount() {
		return this.getCount("Step");
	}

	@Override
	public int getKeyWordCount() {
		return this.getCount("KeyWords");
	}

}
