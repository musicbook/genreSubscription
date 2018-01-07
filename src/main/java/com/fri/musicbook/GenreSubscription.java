package com.fri.musicbook;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;

@Entity(name = "genresubscription")
@NamedQueries(value =
        {
                @NamedQuery(name = "GenreSubscription.getAll", query = "SELECT c FROM genresubscription c")
        })
@UuidGenerator(name = "idGenerator")
public class GenreSubscription {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "id_user")
    private String id_user;

    @Column(name = "id_genre")
    private String id_genre;


    public String getId_genre() {
        return id_genre;
    }

    public void setId_genre(String id_genre) {
        this.id_genre = id_genre;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
