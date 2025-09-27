package org.example.squares.core;

public enum Color {
    W, B;
    public Color opponent() { return this == W ? B : W; }
}
