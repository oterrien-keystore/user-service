package com.ote.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Disabled
public class DerecursiveTest {

    static List<Long> duration = new ArrayList<>();
    static List<Long> duration2 = new ArrayList<>();

    //@RepeatedTest(1000)
    @Test
    public void testIteration() throws Exception {

        List<Element> elements = new ArrayList<>();

        Element A = new Element("A");
        elements.add(A);

        Element AA = new Element("AA");
        A.elements.add(AA);

        Element AAA = new Element("AAA");
        Element AAB = new Element("AAB");
        AA.elements.add(AAA);
        AA.elements.add(AAB);

        Element AB = new Element("AB");
        A.elements.add(AB);

        Element ABA = new Element("ABA");
        Element ABB = new Element("ABB");
        AB.elements.add(ABA);
        AB.elements.add(ABB);

        Element AC = new Element("AC");
        A.elements.add(AC);

        Element ACA = new Element("ACA");
        Element ACB = new Element("ACB");
        AC.elements.add(ACA);
        AC.elements.add(ACB);

        Element B = new Element("B");
        elements.add(B);

        Element BA = new Element("BA");
        Element BB = new Element("BB");
        B.elements.add(BA);
        B.elements.add(BB);

        Element BAA = new Element("BAA");
        Element BAB = new Element("BAB");
        Element BAC = new Element("BAC");
        BA.elements.add(BAA);
        BA.elements.add(BAB);
        BA.elements.add(BAC);

        Element BBA = new Element("BBA");
        BB.elements.add(BBA);

        Long begin = System.currentTimeMillis();
        displayCode(elements);
        duration.add(System.currentTimeMillis() - begin);

        begin = System.currentTimeMillis();
        displayCode2(elements);
        duration2.add(System.currentTimeMillis() - begin);
    }

    @AfterAll
    public static void computeDuration() {
        System.out.println("Avg Duration: " + duration.stream().mapToDouble(a -> a).average());
        System.out.println("Avg Duration2: " + duration2.stream().mapToDouble(a -> a).average());
    }

    public void displayCode(List<Element> elems) {
        elems.stream().forEach(p -> {
            System.out.println(p.getCode());
            String code = p.getCode();
            if (!p.getElements().isEmpty()) {
                displayCode(p.getElements());
            }
        });
    }

    public void displayCode2(List<Element> elements) {
        Deque<Current<Element>> stack = new ArrayDeque<>();

        int i = 0;
        Element current = elements.get(0);
        stack.push(new Current<>(current, 0, elements));

        while (!stack.isEmpty()) {
            Current<Element> currentElement = stack.pop();
            i = currentElement.index;
            current = currentElement.element;
            elements = currentElement.list;
            System.out.println(current.getCode());
            String code = current.getCode();
            if (!current.getElements().isEmpty()) {
                if (i + 1 < elements.size()) {
                    stack.add(new Current<>(elements.get(i + 1), i + 1, elements));
                }
                elements = current.getElements();
                i = 0;
                current = elements.get(i);
                stack.add(new Current<>(current, i, elements));
            } else {
                if (i + 1 < elements.size()) {
                    stack.add(new Current<>(elements.get(i + 1), i + 1, elements));
                }
            }
        }
    }

    @Data
    @RequiredArgsConstructor
    class Element {
        private final String code;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private final List<Element> elements = new ArrayList<>();
    }


    @RequiredArgsConstructor
    private class Current<T> {
        private final T element;
        private final int index;
        private final List<T> list;
    }
}
