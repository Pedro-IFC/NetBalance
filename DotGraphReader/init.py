import os
import glob
import shutil
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from graphviz import Source

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
    imagens.append(nome_png + ".png")

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
