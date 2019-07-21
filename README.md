# # # # # # # # # # # # # # # # # # # # #
# # # # # application.properties # # # #
# # # # # # # # # # # # # # # # # # # # #
#
#
# # # = Basic Spring config
server.port=8080
# # # = Basic database connection
spring.datasource.url=jdbc:mysql://localhost:3306/bt?useSSL=false&createDatabaseIfNotExist=true
# # # = Database username
spring.datasource.username=root
# # # = Database password
spring.datasource.password=root
# # # = Keep the connection alive if idle for a long time (needed in production)
spring.datasource.testWhileIdle=true spring.datasource.validationQuery=SELECT 1
# # # = Sql log, for debugging
spring.jpa.show-sql=false
# # #= Hibernate ddl auto (create, create-drop, update)
spring.jpa.hibernate.ddl-auto=update
# # # = SQL dialect
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
# # # = Spring Security / Queries for AuthenticationManagerBuilder
spring.queries.users-query=select user_email, user_password, user_enabled from user where user_email=?
#
spring.queries.roles-query=select user_email, user_role from user where user_email=?
# # # = Spring Security session max-age
server.servlet.session.cookie.max-age=24h