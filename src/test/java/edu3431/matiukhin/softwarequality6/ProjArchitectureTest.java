package edu3431.matiukhin.softwarequality6;/*
@author sasha
@project SoftwareQuality6
@class ProjArchitectureTest
@version 1.0.0
@since 21.04.2025 - 12 - 33
*/

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import com.tngtech.archunit.junit.ArchTest;
import edu3431.matiukhin.softwarequality6.model.Item;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Method;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringAllDependencies;
import static org.junit.jupiter.api.AssertionsKt.fail;

@SpringBootTest
public class ProjArchitectureTest {
    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("edu3431.matiukhin.softwarequality6");
    }

    @Test
    void shouldFollowedLayerArchitecture() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..") // ← исправлено
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")

                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("out of arch rules")
                .check(applicationClasses);
    }


    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .because("out of arch rules")
                .check(applicationClasses);

    }

    @Test
    void contollerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);

    }

    @Test
    void contollerClassesShouldBeAnnotatedByControllerClass() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .check(applicationClasses);

    }

    @Test
    void repositoryShouldBeInterface() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beInterfaces()
                .check(applicationClasses);
    }

    @Test
    void anyControllerFieldShouldNotBeAnnotateAutowired() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }


    @Test
    void modelFieldsShoudBePrivate() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..model..")
                .should().notBePublic()
                .because("smth")
                .check(applicationClasses);
    }

    @Test
    void layersShouldHaveOnlyOneDirectionDependencies() {
        noClasses()
                .that().resideInAnyPackage("..controller..")
                .should()
                .dependOnClassesThat().resideInAnyPackage("..repository..")
                .check(applicationClasses);
    }

    @Test
    void noControllerShouldDependOnRepository() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat().resideInAPackage("..repository..")
                .check(applicationClasses);
    }


    @Test
    void modelsShouldNotDependOnOtherLayers() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should()
                .dependOnClassesThat().resideInAnyPackage("..controller..", "..service..", "..repository..")
                .check(applicationClasses);
    }

    @Test
    void servicesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }

    @Test
    void serviceMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should()
                .bePublic()
                .check(applicationClasses);
    }


    @Test
    void loggingShouldBeOnlyInServiceAndController() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should()
                .dependOnClassesThat().resideInAPackage("org.slf4j..")
                .check(applicationClasses);
    }

    @Test
    void shouldUseInterfacesNotImplementations() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .dependOnClassesThat().areInterfaces()
                .check(applicationClasses);
    }
}


















