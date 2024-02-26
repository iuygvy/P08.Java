// Bartosz Bugajski - 5
import java.util.Scanner;

/*
Zadanie polega na znalezieniu w nieposortowanej tablicy elementu na danej pozycji, gdyby byla ona posortowana niemalejaco.
W tym celu uzywam metody magicFives, ktora jest lekko zmodyfikowanym algorytmem magicznych piatek przedstawionym na wykladzie.
Modyfikacja polega na tym, ze nie wykorzystuje dodatkowej pamieci.
Nie tworze tablicy na mediany - po prostu zamieniam je miejscami z pierwszymi elementami danej juz tablicy.
Nie tworze trzech tablic na podzbiory glownej tablicy (mniejsze, rowne oraz wieksze medianie median), po prostu wstawiam
elementy mniejsze na poczatek tablicy, a elementy wieksze na koniec. Sila rzeczy elementy rowne zostaja w srodku tablicy.
Algorytm ten w pesymistycznym przypadku ma taka sama zlozonosc jak ten przedstawiony na wykladzie, a wiec liniowa.
*/

public class Source
{
    static int length; // dlugosc tablicy
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        int sets = sc.nextInt(); // ilosc zestawow
        for(int i = 0; i < sets; i++)
        {
            length = sc.nextInt(); // wczytanie dlugosci tablicy
            int[] arr = new int[length]; // stworzenie tablicy o zadanej dlugosci
            for(int j = 0; j < length; j++)
            {
                arr[j] = sc.nextInt(); // wczytanie wartosci tablicy
            }
            int inq = sc.nextInt(); // wczytanie ilosci zapytan
            for(int j = 0; j < inq; j++)
            {
                int toFind = sc.nextInt(); // wczytanie indeksu szukanego elementu w posortowanej niemalejąco tablicy
                if(toFind <= length && toFind >= 1) // jeśli indeks znajduje się w tablicy to wywolujemy metode
                {
                    System.out.println(toFind + " " + magicFives(arr, 0, length - 1, toFind - 1));
                }
                else System.out.println(toFind + " brak"); // w innym przypadku wypisujemy brak
            }
        }
    }

    static void swap(int[] arr, int i1, int i2) // zamiana w tablicy wartosci pod indeksami i1 i i2
    {
        int temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    static void insertionSort(int[] arr, int l, int r) // insertion sort dla odcinka [l..r] w tablicy
    {
        for(int i = 1; i <= r; i++)
        {
            int key = arr[i];
            int j = i - 1;
            while(j >= l && arr[j] > key)
            {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // lekko zmodyfikowany algorytm magicznych piatek przedstawiony na wykladzie
    static int magicFives(int[] arr, int l, int r, int toFind)
    {
        int len = r - l + 1; // dlugosc odcinka tablicy

        // jesli dlugosc odcinka jest mniejsza od dlugosci calej tablicy to uzywamy insertion sorta i zwracamy szukany element
        if(len <= length)
        {
            insertionSort(arr, l, r);
            return arr[l + toFind];
        }

        // obliczamy liczbe piatek na ktore podzielimy tablice
        int numOfFives = len / 5;
        if(len % 5 != 0) numOfFives++;

        // sortujemy kolejne odcinki dlugosci 5 w tablicy za pomoca insertion sort
        // po kazdym posortowaniu wstawiamy mediane danego odcinka na poczatek tablicy
        for(int i = l + 1; i < numOfFives; i++)
        {
            insertionSort(arr, 5 * (i - 1), 5 * i - 1);
            swap(arr, (5*(i-1) + 5*i-1)/2, i-1);
        }
        // to samo robimy dla ostatniego odcinka (ktory moze miec dlugosc mniejsza niz 5)
        insertionSort(arr, 5 * (numOfFives - 1), r);
        swap(arr, (5*(numOfFives-1) + r)/2, numOfFives-1);

        // rekurencyjnie wyznaczamy mediane median
        int M = magicFives(arr, l, numOfFives - 1, numOfFives/2);

        // dzielimy tablice na 3 podzbiory:
        // na poczatku same liczby mniejsze od naszego M
        // nastepnie liczby rowne M
        // na koncu liczby wieksze od M
        int s1 = l - 1; // indeks konca odcinka liczb mniejszych od M
        int s2 = r + 1; // indeks poczatku odcinka liczb wiekszych od M
        int i = l;
        while(i < s2)
        {
            // Przez kazdy element przechodzimy tylko raz, co daje zlozonosc O(n), a wiec taka sama jak z uzyciem
            // dodatkowych tablic
            if(arr[i] > M)
            {
                // jesli sprawdzany element > M to zamieniamy go z elementem na koncu i zmniejszamy s2
                swap(arr, i, --s2);
            }
            else if(arr[i] < M)
            {
                // jesli sprawdzany element < M to zamieniamy go z elementem na poczatku i zwiekszamy s1
                // od razu mozemy tez zwiekszyc i, bo element z ktorym zamieniamy moze byc wylacznie == M
                swap(arr, i++, ++s1);
            }
            // jesli sprawdzany element == M to tylko zwiekszamy i
            else i++;
        }
        // jesli indeks szukanego elementu jest mniejszy niz dlugosc pierwszego odcinka, to szukamy go w pierwszym odcinku
        if(toFind <= s1 - l) return magicFives(arr, l, s1, toFind);

        // jesli indeks szukanego elementu jest mniejszy niz dlugosc drugiego odcinka, to szukamy go w drugin odcinku
        if(toFind <= s1 - l + r - s2) return M;

        // w innym przypadku szukamy go w trzecim odcinku
        return magicFives(arr, s2, r, toFind - s2);
    }
}

/*

INPUT:
5
30
12 34 3 46 43 24 33 27 2 13 25 26 46 24 42 20 47 26 48 1 39 19 20 45 38 2 8 18 30 31
5
1 10 40 30 0
1
1
3
0 1 2
10
296579 459990 237253 343767 519978 553559 113939 463074 725132 914826
5
1 2 3 4 5
10
10 9 8 7 6 5 4 3 2 1
10
1 2 3 4 5 6 7 8 9 10
100
40 50 33 33 49 92 3 8 82 96
28 22 28 78 95 27 97 23 34 60
75 54 81 80 50 73 93 7 81 73
50 93 87 58 37 12 21 87 37 60
53 39 96 27 67 29 51 24 92 41
33 49 9 72 53 11 80 59 30 27
52 42 94 47 45 64 28 83 33 87
69 10 4 60 98 78 41 89 85 15
17 25 84 59 78 39 46 14 17 31
87 30 41 44 34 45 38 15 89 57
20
89 84 100 101 1 0 16 68 32 99 93 78 15 57 46 12 10 22 40 8

OUTPUT:
1 1
10 20
40 brak
30 48
0 brak
0 brak
1 1
2 brak
1 113939
2 237253
3 296579
4 343767
5 459990
1 1
2 2
3 3
4 4
5 5
6 6
7 7
8 8
9 9
10 10
89 89
84 85
100 98
101 brak
1 3
0 brak
16 23
68 67
32 33
99 97
93 93
78 80
15 22
57 53
46 45
12 17
10 15
22 28
40 40
8 12

 */