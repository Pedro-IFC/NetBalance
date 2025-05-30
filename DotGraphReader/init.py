import os
import glob
import shutil
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from graphviz import Source
from PIL import Image, ImageOps

tamanho_padrao = (800, 600)  # largura, altura
cor_fundo = (255, 255, 255)  # Branco (ou use (0, 0, 0) para preto)

pasta = "C:\\Users\\pdani\\Documents\\NetBalance\\grafos"
arquivos_dot = sorted(glob.glob(os.path.join(pasta, "*.dot")))
pasta_img = "imgs_tmp"

if os.path.exists(pasta_img):
    shutil.rmtree(pasta_img)
os.makedirs(pasta_img, exist_ok=True)

imagens = []
for i, caminho_dot in enumerate(arquivos_dot):
    nome_png = os.path.join(pasta_img, f"grafo_{i:03d}")
    src = Source.from_file(caminho_dot)
    src.format = "png"
    src.render(filename=nome_png, cleanup=True)
    caminho_img = nome_png + ".png"

    # Abrir e ajustar com centralização
    with Image.open(caminho_img) as im:
        im = ImageOps.contain(im, tamanho_padrao)  # Redimensiona mantendo proporção
        fundo = Image.new("RGB", tamanho_padrao, cor_fundo)
        pos = (
            (tamanho_padrao[0] - im.width) // 2,
            (tamanho_padrao[1] - im.height) // 2
        )
        fundo.paste(im, pos)
        fundo.save(caminho_img)

    imagens.append(caminho_img)



indice_atual = {'valor': 0}

def atualizar_imagem():
    img = mpimg.imread(imagens[indice_atual['valor']])
    img_plot.set_data(img)
    ax.set_title(f"Imagem {indice_atual['valor'] + 1} de {len(imagens)}", fontsize=12)
    fig.canvas.draw()

def ao_pressionar_tecla(event):
    if event.key == 'right':
        if indice_atual['valor'] < len(imagens) - 1:
            indice_atual['valor'] += 1
            atualizar_imagem()
    elif event.key == 'left':
        if indice_atual['valor'] > 0:
            indice_atual['valor'] -= 1
            atualizar_imagem()

fig, ax = plt.subplots()
img_plot = ax.imshow(mpimg.imread(imagens[0]))
ax.set_title(f"Imagem 1 de {len(imagens)}", fontsize=12)
plt.axis('off')

fig.canvas.mpl_connect('key_press_event', ao_pressionar_tecla)

plt.show()
