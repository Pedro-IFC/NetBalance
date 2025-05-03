import os
import glob
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from graphviz import Source

pasta = "C:\\Users\\pdani\\Documents\\NetBalance\\grafos"
arquivos_dot = sorted(glob.glob(os.path.join(pasta, "*.dot")))

pasta_img = "imgs_tmp"
os.makedirs(pasta_img, exist_ok=True)

imagens = []

for i, caminho_dot in enumerate(arquivos_dot):
    nome_png = os.path.join(pasta_img, f"grafo_{i:03d}")
    src = Source.from_file(caminho_dot)
    src.format = "png"
    src.render(filename=nome_png, cleanup=True)
    imagens.append(nome_png + ".png")

fig, ax = plt.subplots()
img_plot = ax.imshow(mpimg.imread(imagens[0]))
ax.set_title(f"Imagem 1 de {len(imagens)}", fontsize=12)
plt.axis('off')

for idx, img_path in enumerate(imagens[1:], start=2):
    img = mpimg.imread(img_path)
    img_plot.set_data(img)
    ax.set_title(f"Imagem {idx} de {len(imagens)}", fontsize=12)
    plt.pause(1)

plt.show()
