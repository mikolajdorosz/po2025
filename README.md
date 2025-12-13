Projekt laboratoryjny Samochod (symulacja) - GUI
===============================================

Scene Builder - aplikacja zewnętrzna:
https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html


Diagramy UML
------------

### Diagram przypadków użycia

```plantuml
@startuml

left to right direction
actor "Użytkownik" as User

package "Aplikacja Symulator Samochodu" {

    package "Zarządzanie Flotą" {
        usecase "Wybór samochodu z listy" as UC_Select
        usecase "Dodanie nowego samochodu" as UC_Add
        usecase "Usunięcie samochodu" as UC_Remove
        usecase "Wprowadzenie danych modelu\n(Model, Rejestracja, Waga, Vmax)" as UC_InputData
    }

    package "Sterowanie Mechaniką" {
        usecase "Włączenie/Wyłączenie silnika" as UC_Engine
        usecase "Zmiana biegu" as UC_Gear
        usecase "Zwiększenie biegu" as UC_GearUp
        usecase "Zmniejszenie biegu" as UC_GearDown
        usecase "Zmiana prędkości" as UC_Speed
        usecase "Przyspieszanie" as UC_SpeedUp
        usecase "Zwalnianie/hamowanie" as UC_SpeedDown
        usecase "Operowanie sprzęgłem" as UC_Clutch
        usecase "Wciśnięcie sprzęgła" as UC_ClutchPress
        usecase "Zwolnienie sprzęgła" as UC_ClutchRelease
    }

    package "Nawigacja i Symulacja" {
        usecase "Wskazanie celu na mapie" as UC_SetTarget
        usecase "Jazda do celu\n(Symulacja ruchu)" as UC_Drive
        usecase "Podgląd aktualnego stanu\n(Prędkość, Obroty, Pozycja)" as UC_View
    }
}

' Relacje aktora z przypadkami użycia
User --> UC_Select
User --> UC_Add
User --> UC_Remove
User --> UC_Engine
User --> UC_Gear
User --> UC_Speed
User --> UC_Clutch
User --> UC_SetTarget
User --> UC_View

' Relacje między przypadkami użycia (Include/Extend/Generalization)
UC_Add ..> UC_InputData : <<include>>
note right of UC_InputData : Walidacja formularza\n(Lab 9)

UC_Gear <|-- UC_GearUp
UC_Gear <|-- UC_GearDown

UC_Speed <|-- UC_SpeedUp
UC_Speed <|-- UC_SpeedDown

UC_Clutch <|-- UC_ClutchPress
UC_Clutch <|-- UC_ClutchRelease

' Kliknięcie na mapie uruchamia logikę jazdy (wątek)
UC_SetTarget ..> UC_Drive : <<include>>
note right of UC_Drive : Wątek (Thread)\naktualizuje pozycję\n(Lab 10)

@enduml
```

### Diagram klas

```plantuml
@startuml

package samochod {
    class Samochod {
        - String nazwa
        - String nrRejestracyjny
        - Silnik silnik
        - SkrzyniaBiegow skrzyniaBiegow
        - Pozycja pozycja
        - boolean running
        + void wlacz()
        + void wylacz()
        + double getWaga()
        + String getNrRejestracyjny()
        + double getPredkosc()
        + SkrzyniaBiegow getSkrzyniaBiegow()
        + Pozycja getPozycja()
        + String getNazwa()
        + void notifyObserwator()
    }

    class Silnik {
        - int moc
        - int obroty
        + void wlacz()
        + void wylacz()
        + int getObroty()
    }

    class SkrzyniaBiegow {
        - int bieg
        + void zwiekszBieg()
        + void zmniejszBieg()
        + int getBieg()
    }

    class Pozycja {
        - double x
        - double y
    }
    
    class Komponent {
        
    }
    
    class Sprzeglo {
        
    }

    interface Obserwator {
        + void aktualizuj()
    }
    
    class SamochodException {
       
    }
}

package samochodgui {
    class SamochodApplication {
        - Stage primaryStage
        - SamochodController samochodController
        + void start(Stage primaryStage)
    }

    class SamochodController {
        - ComboBox<Samochod> samochodComboBox
        - ObservableList<Samochod> samochody
        - Samochod samochod
        - TextField wagaTextField
        - TextField nrRejestracyjnyTextField
        - TextField predkoscTextField
        - TextField nazwaTextField
        - TextField biegTextField
        - Button wlaczButton
        - ImageView carImageView
        - HBox mapa
        - Label welcomeText
        + void initialize()
        + void onDodajSamochod(ActionEvent event)
        + void onUsunSamochod(ActionEvent event)
        + void dodajSamochod(Samochod nowySamochod)
        + void refresh()
        + void onWlacz(ActionEvent actionEvent)
        + void onWylacz(ActionEvent actionEvent)
        + void onZmniejszBieg(ActionEvent actionEvent)
        + void onUjmijGazu(ActionEvent actionEvent)
        + void onZwiekszBieg(ActionEvent actionEvent)
        + void onNacisnijSprzeglo(ActionEvent actionEvent)
        + void onZwolnij(ActionEvent actionEvent)
        + void aktualizuj()
    }

    class NowySamochodController {
        - TextField nazwaTextField
        - TextField nrRejestracyjnyTextField
        - TextField mocSilnikaTextField
        - TextField obrotySilnikaTextField
        - TextField biegiSkrzyniTextField
        - SamochodController samochodController
        + void setSamochodController(SamochodController samochodController)
        + void onDodaj(ActionEvent event)
        + void onAnuluj(ActionEvent event)
    }
}

samochod.Samochod --> samochod.Silnik
samochod.Samochod --> samochod.SkrzyniaBiegow
samochod.Samochod --> samochod.Pozycja
samochod.Samochod --|> Thread
samochod.Samochod *-- samochod.Obserwator
samochod.Silnik --|> samochod.Komponent
samochod.SkrzyniaBiegow --|> samochod.Komponent
samochod.SkrzyniaBiegow --> samochod.Sprzeglo
samochod.Sprzeglo --|> samochod.Komponent


samochodgui.SamochodController -> samochod.Samochod
samochodgui.SamochodController ..|> samochod.Obserwator

samochodgui.SamochodController <-- samochodgui.NowySamochodController
samochodgui.SamochodApplication --> samochodgui.SamochodController

samochod.SamochodException --|> Exception
samochod.SkrzyniaBiegow -- samochod.SamochodException

hide methods
hide members
@enduml
```

### Diagram sekwencji

```plantuml
@startuml
actor User
participant SamochodApplication
participant SamochodController
participant Samochod
participant Silnik
participant SkrzyniaBiegow

User -> SamochodApplication: start(primaryStage)
SamochodApplication -> SamochodController: initialize()
SamochodController -> Samochod: new Samochod(...)
Samochod -> Silnik: new Silnik(...)
Samochod -> SkrzyniaBiegow: new SkrzyniaBiegow(...)
SamochodController -> Samochod: subscribeObserwator(this)
SamochodController -> Samochod: setItems(samochody)
SamochodController -> Samochod: selectFirst()
@enduml
```

### Diagram sekwencji aktualizacji

```plantuml
@startuml
participant Samochod
participant Obserwator
participant SamochodController
participant Platform

Samochod -> Samochod: notifyObserwator()
Samochod -> Platform: runLater()
Platform -> Obserwator: aktualizuj()
Obserwator -> SamochodController: aktualizuj()
SamochodController -> SamochodController: refresh()
@enduml
```

### Diagram sekwencji dodania samochodu

```plantuml
@startuml
actor User
participant SamochodController
participant NowySamochodController
participant Samochod

User -> SamochodController: onDodajSamochod(event)
SamochodController -> NowySamochodController: setSamochodController(this)
SamochodController -> NowySamochodController: show()

User -> NowySamochodController: onDodaj(event)
NowySamochodController -> Samochod: new Samochod(...)
NowySamochodController -> SamochodController: dodajSamochod(nowySamochod)
SamochodController -> SamochodController: samochody.add(nowySamochod)
SamochodController -> SamochodController: samochodComboBox.select(nowySamochod)
@enduml
```

### Diagram sekwencji usuwania samochodu

```plantuml
@startuml
actor User
participant SamochodController

User -> SamochodController: onUsunSamochod(event)
SamochodController -> SamochodController: samochody.remove(samochod)
SamochodController -> SamochodController: samochodComboBox.selectFirst()
@enduml
