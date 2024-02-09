package dev.danvega.danson.post;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "post")
public class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;

        Integer userId;

        @NotEmpty
        String title;

        @NotEmpty
        String body;

        @Version Integer version;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getUserId() {
                return userId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getBody() {
                return body;
        }

        public void setBody(String body) {
                this.body = body;
        }

        public Integer getVersion() {
                return version;
        }

        public void setVersion(Integer version) {
                this.version = version;
        }

        public Post() {
        }

        public Post(Integer id, Integer userId, String title, String body, Integer version) {
                this.id = id;
                this.userId = userId;
                this.title = title;
                this.body = body;
                this.version = version;
        }
}

