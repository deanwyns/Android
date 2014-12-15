package hogent.hogentprojecteniii_groep10.interfaces;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Downloader;

import java.util.List;
import java.util.Map;

import hogent.hogentprojecteniii_groep10.models.Address;
import hogent.hogentprojecteniii_groep10.models.Category;
import hogent.hogentprojecteniii_groep10.models.ChildrenResponse;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.LoginToken;
import hogent.hogentprojecteniii_groep10.models.Monitor;
import hogent.hogentprojecteniii_groep10.models.MonitorResponse;
import hogent.hogentprojecteniii_groep10.models.Photo;
import hogent.hogentprojecteniii_groep10.models.Registration;
import hogent.hogentprojecteniii_groep10.models.RegistrationsResponse;
import hogent.hogentprojecteniii_groep10.models.SingleVacationResponse;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.*;

/**
 * De interface die connectie met de server mogelijk maakt via RetroFit.
 */
public interface RestService {

    /**
     * Haalt alle vakanties op
     * @return een object dat de vakanties bevat
     */
    @GET("/vacation")
    VacationResponse getVacationOverview();

    /**
     * Haalt de vakantie op met bepaald ID
     * @param vacationId het id van de vakantie
     * @return de gevonden vakantie
     */
    @GET("/vacation/{vacationId}")
    SingleVacationResponse getVacation(@Path("vacationId") long vacationId);

    /**
     * Zal alle foto's in een album teruggeven voor de gegeven vakantie
     * @param vacationId de id van de vakantie waar foto's van moeten worden opgehaald.
     * @return een lijst van foto's van die vakantie
     */
    @GET("/vacation/{vacationId}/photos")
    List<Photo> getPhotosForVacation(@Path("vacationId") long vacationId);

    /**
     * Zal de logintoken geven die opgehaald wordt van de server
     * @param options Zullen de opties zijn die de request nodig heeft bij het ophalen van een token
     * @return de token voor de ingelogde gebruiker
     */
    @FormUrlEncoded
    @POST("/access_token")
    LoginToken login(@FieldMap Map<String, String> options);

    /**
     * Zal een ouder registreren op de server
     * @param options de gegevens die de server nodig heeft om een gebruiker aan te maken
     * @param callback
     */
    @FormUrlEncoded
    @POST("/user")
    void register(@FieldMap Map<String, String> options, Callback<String> callback);

    /**
     * Zal een kind aanmaken op de server
     * @param options de gegevens die de server nodig heeft om een kind aan te maken
     * @param callback
     */
    @FormUrlEncoded
    @POST("/user/me/addchild")
    void addChild(@FieldMap Map<String, String> options , Callback<String> callback);

    /**
     * Geeft de lijst van kinderen terug van de huidig ingelogde persoon.
     * Moet in de header de login token meekregen. Gebeurt hier via een interceptor.
     * @return de lijst van kinderen van dit account.
     */
    @GET("/user/me/children")
    ChildrenResponse getChildren();

    /**
     * Haalt alle registraties op van een bepaald kind.
     * Verwacht ook een logintoken met een interceptor
     * @param childId het id van het kind
     * @return
     */
    @GET("/user/me/{childId}/registrations")
    RegistrationsResponse getRegistrationsForChild(@Path("childId") long childId);

    @FormUrlEncoded
    @POST("/user/me/{childId}/register")
    void registerChild(@FieldMap Map<String, String> registerValues, @Path("childId") long childId, Callback<Response> callback);

    /**
     * Methode voor een vakantie te liken.
     * Verwacht ook een logintoken via de interceptor.
     * @param vacationId het id van de vakantie
     */
    @POST("/user/me/{vacationId}/like")
    void likeVacation(@Path("vacationId") long vacationId, Callback<Response> callback);

    /**
     * Haalt alle categorieën op waar vakanties aan gelinkt kunnen zijn.
     * @return de lijst van bestaande categorieën
     */
    @GET("/category")
    List<Category> getCategories();

    /**
     * Geeft de categorie terug met dat specifiek id
     * @param categoryId het id dat gezocht wordt
     * @return de categorie met bepaald id
     */
    @GET("/category/{id}")
    Category getSpecificCategory(@Path("id") int categoryId);

    /**
     * Zal een lijst van monitors geven op basis van een bepaalde zoekstring.
     * @param searchString de zoekstring waarop gezocht wordt
     * @param callback de callback die wordt uitgevoerd bij het krijgen van een lijst van monitoren
     */
    @GET("/monitor/search")
    void findMonitors(@Query("search_string") String searchString, Callback<MonitorResponse> callback);


}
