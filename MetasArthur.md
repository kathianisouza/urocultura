## METAS EM ABERTO (26/09/12) ##
  * Aplicar HoughCircles para a detecção da placa de Petri   **(F)**

  * Aplicar HoughLine para a detecção da linha que divide a placa de Petri, para encontramos a parte da placa em que estão as colônias de bactérias **(ED)**

  * Aplicar  eurística na contagem de colônias que se encontram aglomeradas para que em conjunto com as técnicas Template Matching e Hough Circles, haja uma contagem mais precisa dessas colônias **(EA)**


### METAS EM ABERTO DEZEMBRO (13/12-20/12) ###

  * Realizar experimentos, tais como: **(EA)**
    * Ajustar valor Threshold (50 - 100);
    * Realizar testes com e sem Watershed;
    * Realizar testes com filtro de Sobel e Canny;
    * Filtro de Gaussian;
    * Realizar testes com o valor de pixel na contagem (50 - 60).

  * Criar classe para arquivar os resultados. **(F)**

### METAS EM ABERTO DEZEMBRO (29/11-06/12) ###

  * Implementar método que varre todas as imagens em um diretório e gera um arquivo que guarda informações de contagem, tais como o nome da imagem e a contagem de colônias realizada pelo programa. **(F)**

### METAS EM ABERTO NOVEMBRO/DEZEMBRO (29/11-06/12) ###

  * Implementação de uma primeira versão de ambiente para contagem de colonias (Usar como referência plugin Hough Circles (Pistori) )**(F)**
    * Análises:
    * Custo computacional altíssimo, pois varre toda a imagem para       detectar uma colônia de cada vez.
    * Utilizando este mesmo algoritmo, será necessário testar melhores valores de parada da contagem. (Linha: 176).

  * Criação de um banco de artigos sobre Hough Circles. **(F)**

### METAS EM ABERTO NOVEMBRO (22/11-29/11) ###

  * Implementação de uma primeira versão de ambiente para contagem de colonias (Usar como referência plugin Hough Circles (Pistori) )**(EA)**
  * Migrar código plugin Hough Circles (Pistori) src,plugins,hough, para svn. **(F)**
  * Migrar imagens para svn. **(F)**
  * Migrar código gerador de amostras para svn. **(F)**
  * Criação de um banco de artigos sobre Hough Circles. **(F)**
  * Remover os comentário da wiki. **(F)**

### METAS EM ABERTO NOVEMBRO (15/11-22/11) ###

Gerador de amostras concluído. Um detalhe a ser corrigido ao instalar o plugin, pois o mesmo não se localiza na aba de plugins do ImageJ ao ser instalado.

Finalizando o banco de amostras, algumas imagens com problemas de iluminação, duplicando algumas colônias por causa do reflexo gerado pela borda da placa de Petri.

Estudo de artigo para a compreensão de melhores técnicas a serem utilizadas usando HCT (Hough Circle Transform).


### METAS EM ABERTO SETEMBRO (27/09-04/10) ###

  * Criação do gerador de amostras. **(F)**
  * Geração e organização das amostras de urocultura. **(F)**


















_Situação: Em desenvolvimento (ED) / Em aberto (EA) / Finalizado (F)_