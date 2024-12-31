package org.example.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void reserveTable() {
        Table table = new Table(4, 1);
        table.reserveTable();
        assertEquals("RESERVED", table.getStatus().toString());
    }

    @Test
    void occupyTable() {
        Table table = new Table(4, 1);
        table.occupyTable();
        assertEquals("OCCUPIED", table.getStatus().toString());
    }

    @Test
    void freeTable() {
        Table table = new Table(4, 1);
        table.freeTable();
        assertEquals("FREE", table.getStatus().toString());
    }
}