package com.example;

import com.example.dao.ContactDao;
import com.example.dao.ContactDao.Contact;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Ajenda di Kontatu - Aplikason CLI
 *
 * Dezafiu konpletu di tutorial JDBC:
 * - Menu interativu
 * - Operason CRUD konpletu
 * - Validason di input
 * - Jeston di éru
 */
public class ContactAgendaApp {

    private final ContactDao dao = new ContactDao();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new ContactAgendaApp().run();
    }

    public void run() {
        System.out.println("\n========================================");
        System.out.println("    AJENDA DI KONTATU - Skola.dev");
        System.out.println("========================================\n");

        boolean running = true;
        while (running) {
            showMenu();
            int option = readOption();

            switch (option) {
                case 1 -> listContacts();
                case 2 -> findContact();
                case 3 -> createContact();
                case 4 -> updateContact();
                case 5 -> deleteContact();
                case 0 -> {
                    running = false;
                    System.out.println("\nAté lógu! Obrigadu pa uza Ajenda di Kontatu.\n");
                }
                default -> System.out.println("\nOpson inválidu. Tenta novamenti.\n");
            }
        }
        scanner.close();
    }

    private void showMenu() {
        System.out.println("=== Menu Prinsipal ===");
        System.out.println("1. Lista tudu kontatu");
        System.out.println("2. Buska kontatu pa ID");
        System.out.println("3. Kria kontatu novu");
        System.out.println("4. Atualiza kontatu");
        System.out.println("5. Apaga kontatu");
        System.out.println("0. Sai");
        System.out.print("\nSkodje opson: ");
    }

    private int readOption() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // === OPERASON 1: Lista tudu kontatu ===
    private void listContacts() {
        System.out.println("\n--- Lista di Kontatus ---\n");

        try {
            List<Contact> contacts = dao.findAll();

            if (contacts.isEmpty()) {
                System.out.println("Nenhun kontatu na bazi di dadus.\n");
                return;
            }

            System.out.println("ID  | Nomi                 | Telefone       | Email");
            System.out.println("----|----------------------|----------------|------------------------");

            for (Contact c : contacts) {
                System.out.printf("% -3d | %-20s | %-14s | %s%n",
                        c.id(),
                        truncate(c.name(), 20),
                        c.phone() != null ? c.phone() : "-",
                        c.email() != null ? c.email() : "-"
                );
            }
            System.out.println();

        } catch (SQLException e) {
            System.err.println("Éru na buska: " + e.getMessage() + "\n");
        }
    }

    // === OPERASON 2: Buska pa ID ===
    private void findContact() {
        System.out.println("\n--- Buska Kontatu pa ID ---\n");
        System.out.print("Inseri ID: ");

        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            Contact contact = dao.findById(id);

            if (contact != null) {
                System.out.println("\nKontatu inkontradu:");
                System.out.println("  ID:       " + contact.id());
                System.out.println("  Nomi:     " + contact.name());
                System.out.println("  Telefone: " + (contact.phone() != null ? contact.phone() : "-"));
                System.out.println("  Email:    " + (contact.email() != null ? contact.email() : "-"));
            } else {
                System.out.println("\nKontatu ku ID " + id + " ka foi inkontradu.");
            }
            System.out.println();

        } catch (NumberFormatException e) {
            System.err.println("ID inválidu. Inseri un númeru.\n");
        } catch (SQLException e) {
            System.err.println("Éru: " + e.getMessage() + "\n");
        }
    }

    // === OPERASON 3: Kria kontatu novu ===
    private void createContact() {
        System.out.println("\n--- Kria Kontatu Novu ---\n");

        System.out.print("Nomi: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.err.println("Nomi é obrigatóriu.\n");
            return;
        }

        System.out.print("Telefone (opsional): ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = null;

        System.out.print("Email (opsional): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = null;
        } else if (!email.contains("@")) {
            System.err.println("Email inválidu - ten ki ten '@'.\n");
            return;
        }

        try {
            long id = dao.create(name, phone, email);
            System.out.println("\nKontatu kriadu ku susesu! ID: " + id + "\n");
        } catch (SQLException e) {
            System.err.println("Éru na kriason: " + e.getMessage() + "\n");
        }
    }

    // === OPERASON 4: Atualiza kontatu ===
    private void updateContact() {
        System.out.println("\n--- Atualiza Kontatu ---\n");
        System.out.print("Inseri ID di kontatu pa atualiza: ");

        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            Contact existing = dao.findById(id);
            if (existing == null) {
                System.out.println("Kontatu ku ID " + id + " ka izisti.\n");
                return;
            }

            System.out.println("\nDadus atual:");
            System.out.println("  Nomi: " + existing.name());
            System.out.println("  Telefone: " + (existing.phone() != null ? existing.phone() : "-"));
            System.out.println("  Email: " + (existing.email() != null ? existing.email() : "-"));
            System.out.println("\nInseri dadus novu (Enter pa manten atual):\n");

            System.out.print("Nomi novu [" + existing.name() + "]: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = existing.name();

            System.out.print("Telefone novu [" + (existing.phone() != null ? existing.phone() : "-") + "]: ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) phone = existing.phone();

            System.out.print("Email novu [" + (existing.email() != null ? existing.email() : "-") + "]: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                email = existing.email();
            } else if (!email.contains("@")) {
                System.err.println("Email inválidu - ten ki ten '@'.\n");
                return;
            }

            if (dao.update(id, name, phone, email)) {
                System.out.println("\nKontatu atualizadu ku susesu!\n");
            } else {
                System.out.println("\nFalha na atualizason.\n");
            }

        } catch (NumberFormatException e) {
            System.err.println("ID inválidu.\n");
        } catch (SQLException e) {
            System.err.println("Éru: " + e.getMessage() + "\n");
        }
    }

    // === OPERASON 5: Apaga kontatu ===
    private void deleteContact() {
        System.out.println("\n--- Apaga Kontatu ---\n");
        System.out.print("Inseri ID di kontatu pa apaga: ");

        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            Contact existing = dao.findById(id);
            if (existing == null) {
                System.out.println("Kontatu ku ID " + id + " ka izisti.\n");
                return;
            }

            System.out.println("\nBu ta apaga kontatu:");
            System.out.println("  " + existing.name() + " (" + existing.email() + ")");
            System.out.print("\nKonfirma? (s/n): ");

            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("s") || confirm.equals("sin")) {
                if (dao.delete(id)) {
                    System.out.println("\nKontatu apagadu ku susesu!\n");
                } else {
                    System.out.println("\nFalha na apaga.\n");
                }
            } else {
                System.out.println("\nOperason kanseladu.\n");
            }

        } catch (NumberFormatException e) {
            System.err.println("ID inválidu.\n");
        } catch (SQLException e) {
            System.err.println("Éru: " + e.getMessage() + "\n");
        }
    }

    private String truncate(String str, int maxLen) {
        if (str == null) return "-";
        return str.length() <= maxLen ? str : str.substring(0, maxLen - 3) + "...";
    }
}
