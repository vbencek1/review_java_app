/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.rest.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.vbencek.model.Book;

public class RestOpenLibrary {

    private static final String BASE_URI = "http://openlibrary.org";

    public Book getBookByIsbn(String ISBN) {
        Book book = new Book();
        book.setIsbn(ISBN);
        try {
            Client client = ClientBuilder.newClient();
            String json = client.target(BASE_URI)
                    .path("isbn").path(ISBN + ".json")
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node;

            node = mapper.readTree(json);
            String title = node.path("title").asText() != null ? node.path("title").asText() : "UNKNOWN";
            String authors = "";
            String description = node.path("first_sentence").path("value").asText() != null ? node.path("first_sentence").path("value").asText() : "UNKNOWN";
            String languages = "";
            String numPages = node.path("number_of_pages").asText() != null ? node.path("number_of_pages").asText() : "UNKNOWN";
            String publishDate = node.path("publish_date").asText();
            String publishers = "";

            //Get all publishers and build String
            if (node.path("publishers") instanceof ArrayNode) {
                ArrayNode arrayNodePublishers = (ArrayNode) node.path("publishers");
                Iterator<JsonNode> itrPublishers = arrayNodePublishers.elements();
                while (itrPublishers.hasNext()) {
                    publishers += itrPublishers.next().asText() + " ";
                }
            }else{
                publishers="UNKNOWN";
            }

            //Get all authors and get String from another REST CALL
            if (node.path("authors") instanceof ArrayNode) {
                ArrayNode arrayNodeAuthors = (ArrayNode) node.path("authors");
                Iterator<JsonNode> itrAuthors = arrayNodeAuthors.elements();
                while (itrAuthors.hasNext()) {
                    String keyAuthor = itrAuthors.next().path("key").asText();
                    authors += getAuthorName(keyAuthor) + " ";
                }
            }else{
                authors="UNKNOWN";
            }

            //Get all languages adn get String from another REST CALL
            if (node.path("languages") instanceof ArrayNode) {
                ArrayNode arrayNodeLang = (ArrayNode) node.path("languages");
                Iterator<JsonNode> itrLang = arrayNodeLang.elements();
                while (itrLang.hasNext()) {
                    String keyLang = itrLang.next().path("key").asText();
                    languages += getLanguageCode(keyLang) + " ";
                }
            }else{
                languages="UNKNOWN";
            }
            
            book.setTitle(title);
            book.setAuthors(authors.trim());
            book.setDescription(description);
            book.setLanguageCode(languages.trim());
            book.setNumPages(numPages);
            book.setPublicationDate(reconvertDate(publishDate));
            book.setPublisher(publishers.trim());
        } catch (Exception ex) {
            System.out.println("RestOpenLibrabry ERROR: can't create new book.");
            System.out.println(ex.getMessage());
        }
        return book;
    }

    private String getAuthorName(String authorKey) {
        Client client = ClientBuilder.newClient();
        String json = client.target(BASE_URI)
                .path(authorKey + ".json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json);
            String name = node.path("name").asText();
            return name;
        } catch (IOException ex) {
            System.out.println("RestOpenLibrabry ERROR: can't get authors name.");
            return "";
        }
    }

    private String getLanguageCode(String langKey) {
        Client client = ClientBuilder.newClient();
        String json = client.target(BASE_URI)
                .path(langKey + ".json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json);
            String code = node.path("code").asText();
            return code;
        } catch (IOException ex) {
            System.out.println("RestOpenLibrabry ERROR: can't get lang code.");
            return "";
        }
    }

    private Date reconvertDate(String strDate) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, u", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(strDate, dateFormatter);
            return java.sql.Date.valueOf(date);
        } catch (Exception e) {
            return new Date();
        }
    }
}
