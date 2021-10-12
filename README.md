# SimpleForumApp


## PL

SimpleForumApp to aplikacja dzięki której możemy prowadzić proste forum dyskusyjne.
Aplikacja pozwala użytkownikowi m.in. publikować posty, tworzyć wątki, zarządzać swoim profilem.
Dostępny jest również panel administracyjny, w którym możemy łatwo zarządzać użytkownikami, działami forum i zmieniać wybrane ustawienia.
Prosty konfigurator pozwala szybko przygotować aplikację do pracy, ograniczając znacznie potrzebę edycji istotnych właściwości bezpośrednio w plikach.

### Informacje techniczne

- Aplikacja została przygotowana z wykorzystaniem frameworka Spring.
- Do generowania widoku aplikacji użyty został silnik szablonów Thymeleaf.
- Do stylizacji domyślnego wyglądu zawartości stron aplikacji użyto biblioteki Bootstrap.
- Aplikacja pozwala przechowywać swoje dane na 2 sposoby: W bazie danych MySQL lub w bazie danych H2.
- Do kompilacji oraz uruchomienia aplikacji wymagana jest zainstalowana Java w wersji 11 lub wyższej.


### Kompilacja

Aby skompilować aplikację wykonaj poniższą komendę w wierszu poleceń w katalogu głównym projektu:

`./mvnw clean install`

### Uruchomienie

Aby uruchomić aplikację można użyć poniższego polecenia w wierszu poleceń:
`java -jar <nazwa_pliku>.jar`

Przykład:
`java -jar SimpleForumApp-1.0.jar`
