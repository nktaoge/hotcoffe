square = { it * it }

html.html {
    head {
        title "Hello"
    }
    body {
    	t = new Teste()		
        p "Olá Mundo!!"
        p "Quadrado " + square(9)
        p t.nome + " " + Teste.count++
    }
}