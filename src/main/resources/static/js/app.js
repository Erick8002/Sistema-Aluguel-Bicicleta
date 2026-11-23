import {
  criarAluguel,
  devolverAluguel,
  listarAlugueis,
  listarClientes,
  criarCliente,
  deletarCliente,
  listarBicicletas,
  criarBicicleta,
  deletarBicicleta,
  listarCategoriasBicicleta,
  criarCategoriaBicicleta,
  deletarCategoriaBicicleta,
  listarHistorico,
} from "./api.js";

// ==========================================
// 1. NAVEGAÇÃO ENTRE ABAS
// ==========================================
let abaAtual = "alugueis";

const navItems = document.querySelectorAll(".nav-item[data-tab]");
const pageTitle = document.querySelector("header h1");
const pageSubtitle = document.querySelector("header p");
const apiBadge = document.querySelector(".api-badge");
const btnAtualizar = document.querySelector("#btn-atualizar");

const abasConfig = {
  alugueis: {
    titulo: "Gestão de Aluguéis",
    subtitulo: "Acompanhe locações ativas, devoluções e valores calculados",
    apiLabel: "API: /alugueis",
    carregar: carregarAlugueis,
  },
  clientes: {
    titulo: "Gestão de Clientes",
    subtitulo: "Cadastre, consulte e gerencie os clientes da locadora",
    apiLabel: "API: /clientes",
    carregar: carregarClientes,
  },
  bicicletas: {
    titulo: "Gestão de Bicicletas",
    subtitulo: "Gerencie a frota disponível para locação",
    apiLabel: "API: /bicicletas",
    carregar: carregarBicicletas,
  },
  categorias: {
    titulo: "Categorias de Bicicleta",
    subtitulo: "Configure categorias e valores por hora",
    apiLabel: "API: /categorias-bicicleta",
    carregar: carregarCategorias,
  },
  historico: {
    titulo: "Histórico",
    subtitulo: "Consulte registros e movimentações do sistema",
    apiLabel: "API: /historicos",
    carregar: carregarHistorico,
  },
};

function trocarAba(nomeAba) {
  abaAtual = nomeAba;

  navItems.forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.tab === nomeAba);
  });

  document.querySelectorAll(".tab-content").forEach((secao) => {
    secao.classList.toggle("hidden", secao.id !== `tab-${nomeAba}`);
  });

  const config = abasConfig[nomeAba];
  if (config) {
    if (pageTitle) pageTitle.textContent = config.titulo;
    if (pageSubtitle) pageSubtitle.textContent = config.subtitulo;
    if (apiBadge) apiBadge.textContent = config.apiLabel;
    config.carregar();
  }
}

navItems.forEach((btn) => {
  btn.addEventListener("click", () => trocarAba(btn.dataset.tab));
});

btnAtualizar.addEventListener("click", () => {
  if (abasConfig[abaAtual]) {
    abasConfig[abaAtual].carregar();
  }
});

// ==========================================
// 2. UTILITÁRIOS
// ==========================================
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

function escaparHtml(valor) {
  return String(valor ?? "").replace(/[&<>"']/g, (caractere) => {
    const entidades = {
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#39;",
    };
    return entidades[caractere];
  });
}

// ==========================================
// 3. MÓDULO: ALUGUÉIS
// ==========================================
const tbody = document.querySelector("#alugueis-tbody");
const form = document.querySelector("#form-aluguel");
const feedbackBox = document.querySelector("#feedback-box");
const contadorRegistros = document.querySelector("#contador-registros");

const metricTotal = document.querySelector("#metric-total");
const metricAtivos = document.querySelector("#metric-ativos");
const metricConcluidos = document.querySelector("#metric-concluidos");
const metricReceita = document.querySelector("#metric-receita");

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
          <td data-label="#ID">#${item.id}</td>
          <td data-label="Cliente">Cliente #${item.clienteId}</td>
          <td data-label="Bicicleta">Bike #${item.bicicletaId}</td>
          <td data-label="Início">${formatarDataHora(item.dataInicio)}</td>
          <td data-label="Fim">${formatarDataHora(item.dataFim)}</td>
          <td data-label="Valor/h">${formatarMoeda(item.valorHoraAplicado)}</td>
          <td data-label="Total">${formatarMoeda(item.valor)}</td>
          <td data-label="Status">
            <span class="status-badge ${emAndamento ? "status-ativo" : "status-finalizado"}">
              ${emAndamento ? "Em andamento" : "Concluído"}
            </span>
          </td>
          <td data-label="Ação">
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

// ==========================================
// 4. MÓDULO: CLIENTES
// ==========================================
const clientesTbody = document.querySelector("#clientes-tbody");
const formCliente = document.querySelector("#form-cliente");
const clienteFeedback = document.querySelector("#cliente-feedback");
const clientesCount = document.querySelector("#clientes-count");

function mostrarFeedbackCliente(mensagem, tipo = "success") {
  if (!clienteFeedback) return;
  clienteFeedback.textContent = mensagem;
  clienteFeedback.className = `feedback ${tipo}`;
}

function limparFeedbackCliente() {
  if (!clienteFeedback) return;
  clienteFeedback.textContent = "";
  clienteFeedback.className = "feedback hidden";
}

function renderizarClientes(clientes) {
  if (!clientesTbody) return;

  if (clientesCount) {
    clientesCount.textContent = `${clientes.length} ${clientes.length === 1 ? "registro" : "registros"}`;
  }

  if (!clientes.length) {
    clientesTbody.innerHTML = `
      <tr>
        <td colspan="5" class="empty-state">Nenhum cliente cadastrado até o momento.</td>
      </tr>
    `;
    return;
  }

  clientesTbody.innerHTML = clientes
    .map((c) => {
      const id = c.id ?? c.clienteId ?? "";
      return `
        <tr>
          <td data-label="#ID">#${id}</td>
          <td data-label="Nome">${c.nome ?? "—"}</td>
          <td data-label="CPF">${c.cpf ?? "—"}</td>
          <td data-label="Telefone">${c.telefone ?? "—"}</td>
          <td data-label="Ação">
            <button type="button" class="btn btn-secondary btn-sm btn-deletar-cliente" data-id="${id}">Excluir</button>
          </td>
        </tr>
      `;
    })
    .join("");
}

async function carregarClientes() {
  try {
    const dados = await listarClientes();
    renderizarClientes(Array.isArray(dados) ? dados : []);
  } catch (err) {
    if (clientesTbody) {
      clientesTbody.innerHTML = `
        <tr>
          <td colspan="5" class="empty-state">Falha ao carregar clientes: ${err.message}</td>
        </tr>
      `;
    }
  }
}

if (formCliente) {
  formCliente.addEventListener("submit", async (event) => {
    event.preventDefault();
    limparFeedbackCliente();

    const payload = {
      nome: document.querySelector("#cliente-nome").value.trim(),
      cpf: document.querySelector("#cliente-cpf").value.trim(),
      telefone: document.querySelector("#cliente-telefone").value.trim(),
    };

    try {
      const criado = await criarCliente(payload);
      mostrarFeedbackCliente(
        `Cliente "${criado.nome ?? payload.nome}" cadastrado (#${criado.id ?? ""}).`,
        "success"
      );
      formCliente.reset();
      await carregarClientes();
    } catch (err) {
      mostrarFeedbackCliente(err.message, "error");
    }
  });
}

if (clientesTbody) {
  clientesTbody.addEventListener("click", async (event) => {
    const botao = event.target.closest(".btn-deletar-cliente");
    if (!botao) return;

    const id = botao.dataset.id;
    botao.disabled = true;
    limparFeedbackCliente();

    try {
      await deletarCliente(id);
      mostrarFeedbackCliente(`Cliente #${id} removido com sucesso.`, "success");
      await carregarClientes();
    } catch (err) {
      mostrarFeedbackCliente(err.message, "error");
      botao.disabled = false;
    }
  });
}

// ==========================================
// 5. MÓDULO: BICICLETAS
// ==========================================
const bicicletasTbody = document.querySelector("#bicicletas-tbody");
const formBicicleta = document.querySelector("#form-bicicleta");
const bicicletaFeedback = document.querySelector("#bicicleta-feedback");
const bicicletasCount = document.querySelector("#bicicletas-count");
const bicicletaCategoria = document.querySelector("#bicicleta-categoria");

const statusBicicletaLabels = {
  DISPONIVEL: "Disponível",
  ALUGADA: "Alugada",
  MANUTENCAO: "Em manutenção",
  INATIVA: "Inativa",
};

function mostrarFeedbackBicicleta(mensagem, tipo = "success") {
  bicicletaFeedback.textContent = mensagem;
  bicicletaFeedback.className = `feedback ${tipo}`;
}

function limparFeedbackBicicleta() {
  bicicletaFeedback.textContent = "";
  bicicletaFeedback.className = "feedback hidden";
}

function atualizarOpcoesCategoria(categorias) {
  if (!bicicletaCategoria) return;

  const selecionada = bicicletaCategoria.value;
  bicicletaCategoria.innerHTML = categorias.length
    ? `<option value="">Selecione uma categoria</option>${categorias
        .map(
          (categoria) =>
            `<option value="${escaparHtml(categoria.id)}">${escaparHtml(categoria.nome)}</option>`
        )
        .join("")}`
    : '<option value="">Cadastre uma categoria primeiro</option>';

  if (categorias.some((categoria) => String(categoria.id) === selecionada)) {
    bicicletaCategoria.value = selecionada;
  }
}

function renderizarBicicletas(bicicletas) {
  bicicletasCount.textContent = `${bicicletas.length} ${
    bicicletas.length === 1 ? "registro" : "registros"
  }`;

  if (!bicicletas.length) {
    bicicletasTbody.innerHTML = `
      <tr>
        <td colspan="5" class="empty-state">Nenhuma bicicleta cadastrada até o momento.</td>
      </tr>
    `;
    return;
  }

  bicicletasTbody.innerHTML = bicicletas
    .map((bicicleta) => {
      const status = statusBicicletaLabels[bicicleta.status] || bicicleta.status || "—";
      const classeStatus = String(bicicleta.status || "").toLowerCase();
      return `
        <tr>
          <td data-label="#ID">#${escaparHtml(bicicleta.id)}</td>
          <td data-label="Modelo">${escaparHtml(bicicleta.modelo)}</td>
          <td data-label="Categoria">${escaparHtml(bicicleta.categoriaNome)}</td>
          <td data-label="Status">
            <span class="status-badge status-bicicleta-${escaparHtml(classeStatus)}">${escaparHtml(status)}</span>
          </td>
          <td data-label="Ação">
            <button type="button" class="btn btn-secondary btn-sm btn-deletar-bicicleta" data-id="${escaparHtml(bicicleta.id)}">Excluir</button>
          </td>
        </tr>
      `;
    })
    .join("");
}

async function carregarBicicletas() {
  try {
    const bicicletas = await listarBicicletas();
    renderizarBicicletas(Array.isArray(bicicletas) ? bicicletas : []);
  } catch (err) {
    bicicletasTbody.innerHTML = `
      <tr>
        <td colspan="5" class="empty-state">Falha ao carregar bicicletas: ${escaparHtml(err.message)}</td>
      </tr>
    `;
  }

  try {
    const categorias = await listarCategoriasBicicleta();
    atualizarOpcoesCategoria(Array.isArray(categorias) ? categorias : []);
  } catch (err) {
    atualizarOpcoesCategoria([]);
    mostrarFeedbackBicicleta(`Falha ao carregar categorias: ${err.message}`, "error");
  }
}

if (formBicicleta) {
  formBicicleta.addEventListener("submit", async (event) => {
    event.preventDefault();
    limparFeedbackBicicleta();

    const payload = {
      modelo: document.querySelector("#bicicleta-modelo").value.trim(),
      categoriaId: Number(bicicletaCategoria.value),
    };

    try {
      const criada = await criarBicicleta(payload);
      mostrarFeedbackBicicleta(`Bicicleta "${criada.modelo}" cadastrada (#${criada.id}).`);
      formBicicleta.reset();
      await carregarBicicletas();
    } catch (err) {
      mostrarFeedbackBicicleta(err.message, "error");
    }
  });
}

if (bicicletasTbody) {
  bicicletasTbody.addEventListener("click", async (event) => {
    const botao = event.target.closest(".btn-deletar-bicicleta");
    if (!botao) return;

    const id = botao.dataset.id;
    botao.disabled = true;
    limparFeedbackBicicleta();

    try {
      await deletarBicicleta(id);
      mostrarFeedbackBicicleta(`Bicicleta #${id} removida com sucesso.`);
      await carregarBicicletas();
    } catch (err) {
      mostrarFeedbackBicicleta(err.message, "error");
      botao.disabled = false;
    }
  });
}

// ==========================================
// 6. MÓDULO: CATEGORIAS DE BICICLETA
// ==========================================
const categoriasTbody = document.querySelector("#categorias-tbody");
const formCategoria = document.querySelector("#form-categoria");
const categoriaFeedback = document.querySelector("#categoria-feedback");
const categoriasCount = document.querySelector("#categorias-count");

function mostrarFeedbackCategoria(mensagem, tipo = "success") {
  categoriaFeedback.textContent = mensagem;
  categoriaFeedback.className = `feedback ${tipo}`;
}

function limparFeedbackCategoria() {
  categoriaFeedback.textContent = "";
  categoriaFeedback.className = "feedback hidden";
}

function renderizarCategorias(categorias) {
  categoriasCount.textContent = `${categorias.length} ${
    categorias.length === 1 ? "registro" : "registros"
  }`;
  atualizarOpcoesCategoria(categorias);

  if (!categorias.length) {
    categoriasTbody.innerHTML = `
      <tr>
        <td colspan="4" class="empty-state">Nenhuma categoria cadastrada até o momento.</td>
      </tr>
    `;
    return;
  }

  categoriasTbody.innerHTML = categorias
    .map(
      (categoria) => `
        <tr>
          <td data-label="#ID">#${escaparHtml(categoria.id)}</td>
          <td data-label="Nome">${escaparHtml(categoria.nome)}</td>
          <td data-label="Valor por hora">${formatarMoeda(categoria.valorPorHora)}</td>
          <td data-label="Ação">
            <button type="button" class="btn btn-secondary btn-sm btn-deletar-categoria" data-id="${escaparHtml(categoria.id)}">Excluir</button>
          </td>
        </tr>
      `
    )
    .join("");
}

async function carregarCategorias() {
  try {
    const categorias = await listarCategoriasBicicleta();
    renderizarCategorias(Array.isArray(categorias) ? categorias : []);
  } catch (err) {
    atualizarOpcoesCategoria([]);
    categoriasTbody.innerHTML = `
      <tr>
        <td colspan="4" class="empty-state">Falha ao carregar categorias: ${escaparHtml(err.message)}</td>
      </tr>
    `;
  }
}

if (formCategoria) {
  formCategoria.addEventListener("submit", async (event) => {
    event.preventDefault();
    limparFeedbackCategoria();

    const payload = {
      nome: document.querySelector("#categoria-nome").value.trim(),
      valorPorHora: Number(document.querySelector("#categoria-valor").value),
    };

    try {
      const criada = await criarCategoriaBicicleta(payload);
      mostrarFeedbackCategoria(`Categoria "${criada.nome}" cadastrada (#${criada.id}).`);
      formCategoria.reset();
      await carregarCategorias();
    } catch (err) {
      mostrarFeedbackCategoria(err.message, "error");
    }
  });
}

if (categoriasTbody) {
  categoriasTbody.addEventListener("click", async (event) => {
    const botao = event.target.closest(".btn-deletar-categoria");
    if (!botao) return;

    const id = botao.dataset.id;
    botao.disabled = true;
    limparFeedbackCategoria();

    try {
      await deletarCategoriaBicicleta(id);
      mostrarFeedbackCategoria(`Categoria #${id} removida com sucesso.`);
      await carregarCategorias();
    } catch (err) {
      mostrarFeedbackCategoria(err.message, "error");
      botao.disabled = false;
    }
  });
}

// ==========================================
// 7. MÓDULO: HISTÓRICO
// ==========================================
const historicoTbody = document.querySelector("#historico-tbody");
const historicoCount = document.querySelector("#historico-count");

const tipoHistoricoLabels = {
  ALUGUEL: "Aluguel",
  DEVOLUCAO: "Devolução",
  MANUTENCAO: "Manutenção",
};

function renderizarHistorico(registros) {
  historicoCount.textContent = `${registros.length} ${
    registros.length === 1 ? "registro" : "registros"
  }`;

  if (!registros.length) {
    historicoTbody.innerHTML = `
      <tr>
        <td colspan="7" class="empty-state">Nenhuma movimentação registrada até o momento.</td>
      </tr>
    `;
    return;
  }

  historicoTbody.innerHTML = registros
    .map((registro) => {
      const tipo = tipoHistoricoLabels[registro.tipo] || registro.tipo || "—";
      return `
        <tr>
          <td data-label="#ID">#${escaparHtml(registro.id)}</td>
          <td data-label="Tipo">${escaparHtml(tipo)}</td>
          <td data-label="Aluguel">#${escaparHtml(registro.aluguelId)}</td>
          <td data-label="Cliente">#${escaparHtml(registro.clienteId)}</td>
          <td data-label="Bicicleta">#${escaparHtml(registro.bicicletaId)}</td>
          <td data-label="Valor">${formatarMoeda(registro.valor)}</td>
          <td data-label="Data e hora">${formatarDataHora(registro.dataHora)}</td>
        </tr>
      `;
    })
    .join("");
}

async function carregarHistorico() {
  try {
    const registros = await listarHistorico();
    renderizarHistorico(Array.isArray(registros) ? registros : []);
  } catch (err) {
    historicoTbody.innerHTML = `
      <tr>
        <td colspan="7" class="empty-state">Falha ao carregar histórico: ${escaparHtml(err.message)}</td>
      </tr>
    `;
  }
}

// ==========================================
// 8. INICIALIZAÇÃO
// ==========================================
carregarAlugueis();