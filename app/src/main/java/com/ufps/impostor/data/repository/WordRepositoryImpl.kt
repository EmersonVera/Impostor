package com.ufps.impostor.data.repository

import com.ufps.impostor.domain.model.GameWord
import com.ufps.impostor.domain.repository.WordRepository

// Baraja General con los subtemas activos por defecto en la web (public/data/palabras.csv)
class WordRepositoryImpl : WordRepository {

    override suspend fun getWords(): Result<List<GameWord>> {
        return Result.success(words)
    }

    private fun entry(topic: String, word: String, hint: String): GameWord {
        // Como en la web, cada fragmento separado por coma o punto y coma es una pista
        val hints = hint.split(",", ";").map { it.trim() }.filter { it.isNotEmpty() }
        return GameWord(word, hints.ifEmpty { listOf("Sin pista disponible") }, topic)
    }

    private val words = listOf(
        entry("Videojuegos", "THE LEGEND OF ZELDA", "Aventura legendaria en el reino de Hyrule explorando templos y santuarios"),
        entry("Videojuegos", "MINECRAFT", "Mundo abierto cúbico de bloques, supervivencia, construcción y crafteo"),
        entry("Videojuegos", "GRAND THEFT AUTO", "Saga de acción criminal y misiones en ciudades abiertas colosales"),
        entry("Videojuegos", "SUPER MARIO BROS", "Fontanero que recorre mundos saltando sobre tuberías para rescatar a la princesa"),
        entry("Videojuegos", "LEAGUE OF LEGENDS", "Juego competitivo de estrategia 5v5 en la Grieta del Invocador"),
        entry("Videojuegos", "ELDEN RING", "Mundo de fantasía oscura con jefes gigantes y combate muy desafiante"),
        entry("Videojuegos", "GOD OF WAR", "Kratos enfrentando dioses mitológicos con sus hachas y espadas del caos"),
        entry("Videojuegos", "VALORANT", "Shooter táctico en primera persona por rondas con agentes y habilidades"),
        entry("Videojuegos", "EA SPORTS FC", "Simulador de fútbol con licencias oficiales, partidos online y cartas Ultimate"),
        entry("Videojuegos", "CYBERPUNK 2077", "Futuro distópico con implantes cibernéticos e historias en Night City"),
        entry("Videojuegos", "FORTNITE", "Battle Royale donde 100 jugadores caen en una isla para construir y combatir"),
        entry("Videojuegos", "CALL OF DUTY", "Shooter bélico militar con partidas multijugador frenéticas y modo Warzone"),
        entry("Videojuegos", "RESIDENT EVIL", "Saga clásica de terror y supervivencia contra zombis y armas biológicas"),
        entry("Videojuegos", "POKÉMON", "Aventura para capturar criaturas con poderes, ganar medallas y ser el mejor"),
        entry("Videojuegos", "AMONG US", "Juego de deducción social en una nave espacial buscando al impostor"),
        entry("Videojuegos", "CLASH ROYALE", "Duelos de cartas y torres en tiempo real con duendes, arqueras y gigantes"),
        entry("Comida", "PIZZA", "Base de masa horneada con salsa de tomate, queso derretido y toppings"),
        entry("Comida", "HAMBURGUESA DOBLE", "Carne jugosa a la parrilla entre dos panes con queso cheddar y vegetales"),
        entry("Comida", "TACOS AL PASTOR", "Tortillas de maíz con carne adobada, trocitos de piña fresca, cebolla y cilantro"),
        entry("Comida", "SUSHI ROLL", "Rollitos tradicionales de arroz sazonado, alga nori, pescado fresco o aguacate"),
        entry("Comida", "AREPA CON QUESO", "Masa redonda de maíz asada a la plancha con queso fundido y mantequilla"),
        entry("Comida", "LASAÑA BOLOÑESA", "Capas de pasta intercaladas con salsa de carne, bechamel y queso gratinado"),
        entry("Comida", "EMPANADAS DE POLLO", "Masa frita crocante rellena de guiso de pollo con papas"),
        entry("Comida", "CEVICHE PERUANO", "Pescado crudo marinado en zumo de limón fresco con cebolla morada y ají"),
        entry("Comida", "RAMEN JAPONÉS", "Tazón hondo de fideos en caldo caliente con huevo marinado y cortes de carne"),
        entry("Comida", "SALCHIPAPA COSTEÑA", "Papas a la francesa fritas con salchichas picadas, queso costeño y salsas"),
        entry("Comida", "BANDEJA PAISA", "Plato típico con frijoles, arroz, chicharrón, carne molida, huevo frito y tajada"),
        entry("Comida", "PERRO CALIENTE", "Salchicha en pan alargado cubierta de salsas, papas trituradas y queso derretido"),
        entry("Peliculas", "HARRY POTTER", "Joven mago huérfano que descubre su destino en el colegio Hogwarts"),
        entry("Peliculas", "TITANIC", "Romance inolvidable a bordo del trasatlántico más famoso antes de hundirse"),
        entry("Peliculas", "INCEPTION", "Misión de espionaje dentro de los sueños y el subconsciente humano"),
        entry("Peliculas", "INTERSTELLAR", "Viaje intergaláctico a través de un agujero negro buscando un planeta habitable"),
        entry("Peliculas", "SPIDER-MAN", "Joven que obtiene poderes arácnidos y aprende que un gran poder conlleva responsabilidad"),
        entry("Peliculas", "EL PADRINO", "Obra maestra sobre el poder, la lealtad y la mafia de la familia Corleone"),
        entry("Peliculas", "AVENGERS ENDGAME", "Superhéroes unidos para recolectar las gemas del infinito y revertir el chasquido"),
        entry("Peliculas", "JURASSIC PARK", "Parque temático donde científicos clonan dinosaurios que pierden el control"),
        entry("Peliculas", "BATMAN EL CABALLERO DE LA NOCHE", "El vigilante de Ciudad Gótica enfrentando el caos provocado por el Guasón"),
        entry("Peliculas", "EL SEÑOR DE LOS ANILLOS", "Viaje épico a Mordor para destruir el anillo único y salvar la Tierra Media"),
        entry("Peliculas", "MATRIX", "Hacker que descubre que el mundo real está dominado por máquinas inteligentes"),
        entry("Peliculas", "SHREK", "Ogro verde de un pantano que rescata a una princesa con la ayuda de un burro parlanchín")
    )
}
