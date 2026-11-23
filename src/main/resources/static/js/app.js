import { criarAluguel, devolverAluguel, listarAlugueis } from "./api.js";

const tbody = document.querySelector("#alugueis-tbody");
const form = document.querySelector("#form-aluguel");
const feedbackBox = document.querySelector("#feedback-box");
const btnAtualizar = document.querySelector("#btn-atualizar");
const contadorRegistros = document.querySelector("#contador-registros");

const metricTotal = document.querySelector("#metric-total");
const metricAtivos = document.querySelector("#metric-ativos");
const metricConcluidos = document.querySelector("#metric-concluidos");
const metricReceita = document.querySelector("#metric-receita");

function formatarMoeda(valor) {
  if (valor === null || valor === undefined || Number.isNaN(Number(valor))) {
    return "—";
  }
  return Number(valor).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}

function formatarDataHora(isoString) {
  if (!isoString) return "—";
  const data = new Date(isoString);
  if (Number.isNaN(data.getTime())) return isoString;
  return data.toLocaleString("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  });
}

function mostrarFeedback(mensagem, tipo = "success") {
  feedbackBox.textContent = mensagem;
  feedbackBox.className = `feedback ${tipo}`;
}

function limparFeedback() {
  feedbackBox.textContent = "";
  feedbackBox.className = "feedback hidden";
}

function atualizarMetricas(alugueis) {
  const total = alugueis.length;
  const ativos = alugueis.filter((item) => !item.dataFim).length;
  const concluidos = total - ativos;
  const receita = alugueis.reduce(
    (soma, item) => soma + (Number(item.valor) || 0),
    0
  );

  metricTotal.textContent = total;
  metricAtivos.textContent = ativos;
  metricConcluidos.textContent = concluidos;
  metricReceita.textContent = formatarMoeda(receita);
  contadorRegistros.textContent = `${total} ${total === 1 ? "registro" : "registros"}`;
}

function renderizarTabela(alugueis) {
  atualizarMetricas(alugueis);

  if (!alugueis.length) {
    tbody.innerHTML = `
      <tr>
        <td colspan="9" class="empty-state">Nenhum aluguel cadastrado até o momento.</td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = alugueis
    .map((item) => {
      const emAndamento = !item.dataFim;
      return `
        <tr>
          <td>#${item.id}</td>
          <td>Cliente #${item.clienteId}</td>
          <td>Bike #${item.bicicletaId}</td>
          <td>${formatarDataHora(item.dataInicio)}</td>
          <td>${formatarDataHora(item.dataFim)}</td>
          <td>${formatarMoeda(item.valorHoraAplicado)}</td>
          <td>${formatarMoeda(item.valor)}</td>
          <td>
            <span class="status-badge ${emAndamento ? "status-ativo" : "status-finalizado"}">
              ${emAndamento ? "Em andamento" : "Concluído"}
            </span>
          </td>
          <td>
            ${
              emAndamento
                ? `<button class="btn btn-secondary btn-sm btn-devolver" data-id="${item.id}" type="button">Devolver</button>`
                : "—"
            }
          </td>
        </tr>
      `;
    })
    .join("");
}

async function carregarAlugueis() {
  try {
    const dados = await listarAlugueis();
    renderizarTabela(Array.isArray(dados) ? dados : []);
  } catch (err) {
    tbody.innerHTML = `
      <tr>
        <td colspan="9" class="empty-state">Falha ao carregar aluguéis: ${err.message}</td>
      </tr>
    `;
  }
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  limparFeedback();

  const clienteId = Number(document.querySelector("#clienteId").value);
  const bicicletaId = Number(document.querySelector("#bicicletaId").value);

  try {
    const criado = await criarAluguel({ clienteId, bicicletaId });
    mostrarFeedback(`Aluguel #${criado.id} registrado com sucesso.`, "success");
    form.reset();
    await carregarAlugueis();
  } catch (err) {
    mostrarFeedback(err.message, "error");
  }
});

tbody.addEventListener("click", async (event) => {
  const botao = event.target.closest(".btn-devolver");
  if (!botao) return;

  const id = botao.dataset.id;
  botao.disabled = true;
  limparFeedback();

  try {
    const atualizado = await devolverAluguel(id);
    mostrarFeedback(
      `Devolução do aluguel #${atualizado.id} registrada (${formatarMoeda(atualizado.valor)}).`,
      "success"
    );
    await carregarAlugueis();
  } catch (err) {
    mostrarFeedback(err.message, "error");
    botao.disabled = false;
  }
});

btnAtualizar.addEventListener("click", carregarAlugueis);

carregarAlugueis();