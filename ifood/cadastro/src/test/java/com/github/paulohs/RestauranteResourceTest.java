package com.github.paulohs;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@DBRider
@QuarkusTest
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTestResource(CadastroTestLifecycleManager.class)
class RestauranteResourceTest {

    @Test
    @DataSet("dataset/yml/restaurante.yml")
    public void testBuscarRestaurantes() {
        String resultado = given()
                .when().get("/restaurantes")
                .then()
                .statusCode(200)
                .extract().asString();
//        Approvals.verifyJson(resultado);
    }

//    @Test
//    public void testList() {
//        String[] names = {"Llewellyn", "James", "Dan", "Jason", "Katrina"};
//        Arrays.sort(names);
//        Approvals.verifyAll("", names);
//    }
}