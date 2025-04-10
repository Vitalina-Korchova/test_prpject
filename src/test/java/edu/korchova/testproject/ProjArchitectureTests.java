package edu.korchova.testproject;

/*
    @author Віталіна
    @project testProject
    @class ProjArchitectureTests
    @version 1.0.0
    @since 10.04.2025 - 18-02
*/

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@SpringBootTest
public class ProjArchitectureTests {

    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("edu.korchova.testproject");
    }

    @Test
    void shouldFollowLayerArchitecture()  {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
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
                .because("controllers should not depend on other controllers")
                .check(applicationClasses);
    }

    @Test
    void servicesShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("services should not depend on controllers")
                .check(applicationClasses);
    }

    @Test
    void repositoriesShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should().dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("repositories should not depend on controllers")
                .check(applicationClasses);
    }

    @Test
    void modelsShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("models should not depend on controllers")
                .check(applicationClasses);
    }


    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .because("repositories should not depend on services")
                .check(applicationClasses);
    }

    @Test
    void  controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldBeNamedXRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .haveSimpleNameEndingWith("Repository")
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(applicationClasses);
    }


    @Test
    void controllerClassesShouldBeAnnotatedByControllerClass() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(org.springframework.stereotype.Service.class)
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldBeAnnotatedWithRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beAnnotatedWith(org.springframework.stereotype.Repository.class)
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
    void anyControllerFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    @Test
    void anyServiceFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    @Test
    void anyRepositoryFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }
    @Test
    void modelFieldsShouldBePrivate() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..model..")
                .should().bePrivate()
                .because("model fields should be encapsulated to follow object-oriented principles")
                .check(applicationClasses);

    }

    @Test
    void modelClassesShouldBeAnnotatedWithDocument() {
        classes()
                .that().resideInAPackage("..model..")
                .should()
                .beAnnotatedWith(org.springframework.data.mongodb.core.mapping.Document.class)
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldNotDependOnController() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldNotDependOnRepository() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..repository..")
                .check(applicationClasses);
    }




}
