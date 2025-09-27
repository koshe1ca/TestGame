package org.example.squares.cli;

import org.example.squares.core.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Optional;

/**
 * Консольная игра "Квадраты".
 *
 * Команды:
 *   GAME N, TYPE1 C1, TYPE2 C2   (TYPE = user|comp, C = W|B, белые ходят первыми)
 *   MOVE X, Y
 *   HELP
 *   EXIT
 */
public class Main {

    enum Type { user, comp }
    static class Player {
        final Type type;
        final Color color;
        Player(Type type, Color color) { this.type = type; this.color = color; }
    }

    static GameState game = null;
    static Player p1, p2;
    static final long AI_SEED = 1234L;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String up = line.toUpperCase(Locale.ROOT);

            try {
                if (up.startsWith("GAME")) {
                    handleGame(line); // передаём ОРИГИНАЛЬНУЮ строку
                } else if (up.startsWith("MOVE")) {
                    handleMove(line);
                } else if (up.equals("HELP")) {
                    printHelp();
                } else if (up.equals("EXIT")) {
                    return;
                } else {
                    System.out.println("Неверная команда. Введите HELP для справки.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Некорректные параметры.");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка: " + e.getMessage());
            }
        }
    }

    /** GAME N, TYPE1 C1, TYPE2 C2 */
    static void handleGame(String cmd) {
        // Выделяем хвост после "GAME"
        String tail = cmd.substring(4).trim();
        if (tail.isEmpty()) throw new IllegalArgumentException("Некорректные параметры команды GAME.");

        // Разделяем по запятым, допускаем лишние пробелы
        String[] parts = tail.split(",");
        if (parts.length != 3) throw new IllegalArgumentException("Некорректные параметры команды GAME.");

        int n = Integer.parseInt(parts[0].trim());

        String[] p1tokens = parts[1].trim().split("\\s+");
        String[] p2tokens = parts[2].trim().split("\\s+");
        if (p1tokens.length != 2 || p2tokens.length != 2)
            throw new IllegalArgumentException("Некорректные параметры игроков.");

        Type t1 = Type.valueOf(p1tokens[0].toLowerCase(Locale.ROOT));
        Color c1 = Color.valueOf(p1tokens[1].toUpperCase(Locale.ROOT));
        Type t2 = Type.valueOf(p2tokens[0].toLowerCase(Locale.ROOT));
        Color c2 = Color.valueOf(p2tokens[1].toUpperCase(Locale.ROOT));

        if (c1 == c2) throw new IllegalArgumentException("Оба игрока не могут играть одним цветом.");

        p1 = new Player(t1, c1);
        p2 = new Player(t2, c2);

        // Белые ходят первыми
        game = new GameState(n, Color.W);

        System.out.println("Новая игра начата");

        // Если первый ход за компьютером — сразу сделать его (и далее, пока comp)
        stepComputerIfNeeded();
    }

    /** MOVE X, Y — ход текущего игрока (если он user) */
    static void handleMove(String cmd) {
        if (game == null) { System.out.println("Нет активной игры. Используйте GAME для старта."); return; }

        String tail = cmd.substring(4).trim();
        String[] xy = tail.split(",");
        if (xy.length != 2) throw new IllegalArgumentException("Некорректные координаты. Ожидается: MOVE X, Y");

        int x = Integer.parseInt(xy[0].trim());
        int y = Integer.parseInt(xy[1].trim());

        Player current = who(game.turn);
        if (current.type != Type.user) { System.out.println("Сейчас ход компьютера."); return; }

        Rules.applyMove(game, new Point(x, y), game.turn);
        if (printIfFinished()) return;

        // Если теперь ход компьютера — дать ему походить (пока comp и игра не закончена)
        stepComputerIfNeeded();
    }

    static Player who(Color c) { return p1.color == c ? p1 : p2; }

    static void stepComputerIfNeeded() {
        while (game != null && game.status == GameStatus.ONGOING && who(game.turn).type == Type.comp) {
            // Если у тебя в core ещё нет AI.java — временно закомментируй блок ниже или добавь AI по нашему плану.
            AI ai = new AI(AI_SEED);
            Optional<AI.Move> mv = ai.chooseMove(game, game.turn);
            if (mv.isEmpty()) break;
            Point p = mv.get().p();
            System.out.println(game.turn + " (" + p.x + ", " + p.y + ")");
            Rules.applyMove(game, p, game.turn);
            if (printIfFinished()) return;
        }
    }

    static boolean printIfFinished() {
        if (game.status == GameStatus.DRAW) {
            System.out.println("Игра окончена. Ничья.");
            return true;
        }
        if (game.status == GameStatus.WIN_W) {
            System.out.println("Игра окончена. Победа W!");
            return true;
        }
        if (game.status == GameStatus.WIN_B) {
            System.out.println("Игра окончена. Победа B!");
            return true;
        }
        return false;
    }

    static void printHelp() {
        System.out.println("Команды:");
        System.out.println("  GAME N, TYPE1 C1, TYPE2 C2");
        System.out.println("    N    — размер поля (целое >= 3)");
        System.out.println("    TYPE — user | comp");
        System.out.println("    C    — W | B (белые ходят первыми)");
        System.out.println("  MOVE X, Y");
        System.out.println("  HELP");
        System.out.println("  EXIT");
        System.out.println();
        System.out.println("Формат вывода хода компьютера: 'C (X, Y)'");
    }
}
