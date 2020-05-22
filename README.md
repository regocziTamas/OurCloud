# OurCloud

## Welcome to OurCloud!

OurCloud is my current pet project that I develop in my free time. In short, it is a Google Drive clone, meaning that users can upload their files to a server, manage and organize them into folders, and also download them on any device with an active internet connection. The project is written in Java 11 and uses Spring Boot on the backend, with PostgreSQL as the persistence layer, Angular 9 on the frontend, and it is served by an Nginx server running on a Raspberry Pi 4 on my desk. The site is available at rtamas.com, you can try it out by logging in as "Guest01" and using "yxcv1234" as password. However, your changes may not be permanent, as the project is still in development. 

## Backend
I chose to use Spring Boot on the backend, as I aimed to gain hands-on experience in a framework that has grown to be an industry standard in Java web applications, and with its 'convention over configuration' principle, it makes the development process quick, as well as it helps to produce maintainable code. The application conforms to the REST architectural style, the responses are pure JSON that are processed and displayed by the frontend, using Angular.

For the database, I chose PostgreSQL, as it is also a popular choice in the industry, and, similarly to Spring Boot, I also had some previous experiences with it from my studies at Codecool. Files and folders naturally form a tree-like structure that I wanted to leverage when storing them in the database. After researching common techniques for storing hierarchical data in a relational database, such as Adjacency List and Nested Sets, I discovered that Postgres' ltree extension ([https://www.postgresql.org/docs/12/ltree.html](https://www.postgresql.org/docs/12/ltree.html)) provides exactly this kind of functionality out-of-the-box, using the 'path enumeration' algorithm. 

As to the security, with the help of the Spring Boot Security module, I implemented a JWT token based authentication, which made it possible for the application to be completely stateless. Of course, the users' passwords still have to be stored, but in a Bcrypt hashed form.

## Frontend
### [https://github.com/regocziTamas/OurCloudFrontEnd](https://github.com/regocziTamas/OurCloudFrontEnd)
Keeping up with the current trends, the HTML is not generated on the server, but a JavaScript framework is used, in this case Angular. I had much fun playing around with the different color and other CSS settings, however, the current look of the application could still definitely use some (or a lot of) beautifying. To make my life easier, I used Google's Material Design components, so the application is a bit more pleasant for the eye.

## DevOps
I wanted to be responsible for the entire life cycle of the application, and therefore, I decided to host it myself. This was the part that slowed down the project the most, as this was the area where I had the least amount of experience. The hardware is a Raspberry Pi 4 running Ubuntu server with a lightweight desktop environment. My intention was to incorporate Docker into the project somehow, just for the sake of experimenting, however, Docker requires a 64-bit OS, and the pre-installed Raspbian OS is only 32-bit, so it had to go.

From version 2.3.0, Spring Boot comes with the option of packaging the application as a layered jar, which is perfect for a Docker image. This separation allows the dependencies, which usually do not change too often, to go in a separate layer when building the Docker image, therefore, they can be cached, which results in considerably faster build times. For now, the fact that the application runs in a Docker container does not add much value to the project, but I definitely learned a lot from it.

The Angular project is built into static HTML, CSS, and JavaScript assets that are served by an Nginx server. The same Nginx server is used as a reverse proxy to direct the requests coming from the frontend to the Spring Boot application. The traffic is SSL encrypted with a self-signed certificate from Let's Encrypt. As the server is using a home network, which usually do not have a static IP address, I am using Cloudflare's dynamic DNS service to ensure that the domain name always points to the right address. I took basic security measures to protect the server by enabling UFW on Ubuntu, and I aimed to apply the most restrictive rules possible.

Pulling from the GitHub repository, building the projects and Docker images, and moving them to the right places can be bothersome, therefore, I automated the whole process using Python and shell scripts. A new version of both projects (Spring Boot and Angular) can be deployed with just one command. The version control is taken care of by Git, of course.  
