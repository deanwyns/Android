package hogent.hogentprojecteniii_groep10.interfaces;

import java.util.List;
import java.util.Map;

import hogent.hogentprojecteniii_groep10.models.Category;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.LoginToken;
import hogent.hogentprojecteniii_groep10.models.Photo;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import retrofit.Callback;
import retrofit.http.*;

/**
 * De interface die connectie met de server mogelijk maakt via RetroFit.
 */
public interface RestService {

    @GET("/vacation")
    VacationResponse getVacationOverview();

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
    @POST("/user/me/children")
    void addChild(@FieldMap Map<String, String> options, Callback<String> callback);

    /**
     * Geeft de lijst van kinderen terug van de huidig ingelogde persoon.
     * Moet in de header de login token meekregen. Gebeurt hier via een interceptor.
     * @return de lijst van kinderen van dit account.
     */
    @GET("/user/me/children")
    List<Kind> getChildren();

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


}
