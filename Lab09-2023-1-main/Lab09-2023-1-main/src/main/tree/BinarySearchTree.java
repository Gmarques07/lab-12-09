package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import estrut.Tree;

public class BinarySearchTree implements Tree {
    private Node raiz;

    public BinarySearchTree() {
        raiz = null;
    }

    private static class Node {
        int valor;
        Node esquerda, direita;
        int altura;

        Node(int item) {
            valor = item;
            esquerda = direita = null;
            altura = 1;
        }
    }

    
    private int altura(Node nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    
    private int fatorDeBalanceamento(Node nodo) {
        return nodo == null ? 0 : altura(nodo.esquerda) - altura(nodo.direita);
    }

    private Node rotacaoDireita(Node y) {
        Node x = y.esquerda;
        Node T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;

        return x;
    }

    private Node rotacaoEsquerda(Node x) {
        Node y = x.direita;
        Node T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    private Node balancear(Node nodo) {
        int balance = fatorDeBalanceamento(nodo);

        if (balance > 1 && fatorDeBalanceamento(nodo.esquerda) >= 0)
            return rotacaoDireita(nodo);

        if (balance > 1 && fatorDeBalanceamento(nodo.esquerda) < 0) {
            nodo.esquerda = rotacaoEsquerda(nodo.esquerda);
            return rotacaoDireita(nodo);
        }

        if (balance < -1 && fatorDeBalanceamento(nodo.direita) <= 0)
            return rotacaoEsquerda(nodo);

        if (balance < -1 && fatorDeBalanceamento(nodo.direita) > 0) {
            nodo.direita = rotacaoDireita(nodo.direita);
            return rotacaoEsquerda(nodo);
        }

        return nodo;
    }

    @Override
    public boolean buscaElemento(int valor) {
        return buscaElemento(raiz, valor);
    }

    private boolean buscaElemento(Node nodo, int valor) {
        if (nodo == null) {
            return false;
        }
        if (valor == nodo.valor) {
            return true;
        }
        return valor < nodo.valor ? buscaElemento(nodo.esquerda, valor) : buscaElemento(nodo.direita, valor);
    }

    @Override
    public int minimo() {
        if (raiz == null) {
            throw new NoSuchElementException("A árvore está vazia");
        }
        return minimo(raiz);
    }

    private int minimo(Node nodo) {
        return nodo.esquerda == null ? nodo.valor : minimo(nodo.esquerda);
    }

    @Override
    public int maximo() {
        if (raiz == null) {
            throw new NoSuchElementException("A árvore está vazia");
        }
        return maximo(raiz);
    }

    private int maximo(Node nodo) {
        return nodo.direita == null ? nodo.valor : maximo(nodo.direita);
    }

    @Override
    public void insereElemento(int valor) {
        raiz = insereElemento(raiz, valor);
    }

    private Node insereElemento(Node nodo, int valor) {
        if (nodo == null) {
            nodo = new Node(valor);
            return nodo;
        }
        if (valor < nodo.valor) {
            nodo.esquerda = insereElemento(nodo.esquerda, valor);
        } else if (valor > nodo.valor) {
            nodo.direita = insereElemento(nodo.direita, valor);
        } else {
            return nodo; 
        }

        nodo.altura = 1 + Math.max(altura(nodo.esquerda), altura(nodo.direita));

        return balancear(nodo);
    }

    @Override
    public void remove(int valor) {
        raiz = remove(raiz, valor);
    }

    private Node remove(Node nodo, int valor) {
        if (nodo == null) {
            return null;
        }
        if (valor < nodo.valor) {
            nodo.esquerda = remove(nodo.esquerda, valor);
        } else if (valor > nodo.valor) {
            nodo.direita = remove(nodo.direita, valor);
        } else {
            if (nodo.esquerda == null) {
                return nodo.direita;
            } else if (nodo.direita == null) {
                return nodo.esquerda;
            }
            nodo.valor = minimo(nodo.direita);
            nodo.direita = remove(nodo.direita, nodo.valor);
        }

        if (nodo == null) {
            return null;
        }

        nodo.altura = 1 + Math.max(altura(nodo.esquerda), altura(nodo.direita));

        return balancear(nodo);
    }

    @Override
    public int[] preOrdem() {
        List<Integer> lista = new ArrayList<>();
        preOrdem(raiz, lista);
        return lista.stream().mapToInt(i -> i).toArray();
    }

    private void preOrdem(Node nodo, List<Integer> lista) {
        if (nodo != null) {
            lista.add(nodo.valor);
            preOrdem(nodo.esquerda, lista);
            preOrdem(nodo.direita, lista);
        }
    }

    @Override
    public int[] emOrdem() {
        List<Integer> lista = new ArrayList<>();
        emOrdem(raiz, lista);
        return lista.stream().mapToInt(i -> i).toArray();
    }

    private void emOrdem(Node nodo, List<Integer> lista) {
        if (nodo != null) {
            emOrdem(nodo.esquerda, lista);
            lista.add(nodo.valor);
            emOrdem(nodo.direita, lista);
        }
    }

    @Override
    public int[] posOrdem() {
        List<Integer> lista = new ArrayList<>();
        posOrdem(raiz, lista);
        return lista.stream().mapToInt(i -> i).toArray();
    }

    private void posOrdem(Node nodo, List<Integer> lista) {
        if (nodo != null) {
            posOrdem(nodo.esquerda, lista);
            posOrdem(nodo.direita, lista);
            lista.add(nodo.valor);
        }
    }
}