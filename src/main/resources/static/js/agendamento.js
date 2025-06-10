function toggleMinutos() {
    const checkboxes = document.getElementById('checkboxesMinutos');
    //var hidden = document.getElementById('minutosHidden');
    // if (document.getElementById('todosMinutos').checked) {
    //     checkboxes.classList.remove("agenda-checkboxes-show");
    //     checkboxes.classList.remove("apenas-minutos-multiplos-de-cinco");
    //     checkboxes.classList.add("agenda-checkboxes-hidden");
    //     hidden.value = '*';
    // }
    if (document.getElementById('minutosMultiplosDeCinco').checked) {
        checkboxes.classList.remove("agenda-checkboxes-hidden");
        checkboxes.classList.add("agenda-checkboxes-show");
        checkboxes.classList.add("apenas-minutos-multiplos-de-cinco");
        var minutos = Array.from(checkboxes.querySelectorAll('.minuto:checked'))
        minutos.forEach(cb => {
            if (cb.value % 5 !== 0) cb.checked = false;
        })
        updateMinutos();
    } else {
        checkboxes.classList.remove("apenas-minutos-multiplos-de-cinco");
        checkboxes.classList.remove("agenda-checkboxes-hidden");
        checkboxes.classList.add("agenda-checkboxes-show");
        updateMinutos();
    }
}

function toggleHoras() {
    var checkboxes = document.getElementById('checkboxesHoras');
    var hidden = document.getElementById('horasHidden');
    if (document.getElementById('todasHoras').checked) {
        checkboxes.classList.remove("agenda-checkboxes-show");
        checkboxes.classList.add("agenda-checkboxes-hidden");
        hidden.value = '*';
    } else {
        checkboxes.classList.remove("agenda-checkboxes-hidden");
        checkboxes.classList.add("agenda-checkboxes-show");
        updateHoras();
    }
}

function toggleDiasMes() {
    var checkboxes = document.getElementById('checkboxesDiasMes');
    var hidden = document.getElementById('diasMesHidden');
    if (document.getElementById('todosDiasMes').checked) {
        checkboxes.classList.remove("agenda-checkboxes-show");
        checkboxes.classList.add("agenda-checkboxes-hidden");
        hidden.value = '*';
    } else {
        checkboxes.classList.remove("agenda-checkboxes-hidden");
        checkboxes.classList.add("agenda-checkboxes-show");
        updateDiasMes();
    }
}

function toggleDiasSemana() {
    var checkboxes = document.getElementById('checkboxesDiasSemana');
    var hidden = document.getElementById('diasSemanaHidden');
    if (document.getElementById('todosDias').checked) {
        checkboxes.classList.remove("agenda-checkboxes-show");
        checkboxes.classList.add("agenda-checkboxes-hidden");
        hidden.value = '*';
    } else {
        checkboxes.classList.remove("agenda-checkboxes-hidden");
        checkboxes.classList.add("agenda-checkboxes-show");
        updateDiasSemana();
    }
}

function toggleMeses() {
    var checkboxes = document.getElementById('checkboxesMeses');
    var hidden = document.getElementById('mesesHidden');
    if (document.getElementById('todosMeses').checked) {
        checkboxes.classList.remove("agenda-checkboxes-show");
        checkboxes.classList.add("agenda-checkboxes-hidden");
        hidden.value = '*';
    } else {
        checkboxes.classList.remove("agenda-checkboxes-hidden");
        checkboxes.classList.add("agenda-checkboxes-show");
        updateMeses();
    }
}

document.querySelectorAll('.minuto').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateMinutos);
});

document.querySelectorAll('.hora').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateHoras);
});

document.querySelectorAll('.dia-mes').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateDiasMes);
});

document.querySelectorAll('.dia-semana').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateDiasSemana);
});

document.querySelectorAll('.mes').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateMeses);
});

function updateMinutos() {
    var selected = Array.from(document.querySelectorAll('.minuto:checked'))
        .map(cb => cb.value);
    document.getElementById('minutosHidden').value =
        selected.length > 0 ? selected.join(',') : '*';
}

function updateHoras() {
    var selected = Array.from(document.querySelectorAll('.hora:checked'))
        .map(cb => cb.value);
    document.getElementById('horasHidden').value =
        selected.length > 0 ? selected.join(',') : '*';
}

function updateDiasMes() {
    var selected = Array.from(document.querySelectorAll('.dia-mes:checked'))
        .map(cb => cb.value);
    document.getElementById('diasMesHidden').value =
        selected.length > 0 ? selected.join(',') : '*';
}

function updateDiasSemana() {
    var selected = Array.from(document.querySelectorAll('.dia-semana:checked'))
        .map(cb => cb.value);
    document.getElementById('diasSemanaHidden').value =
        selected.length > 0 ? selected.join(',') : '*';
}

function updateMeses() {
    var selected = Array.from(document.querySelectorAll('.mes:checked'))
        .map(cb => cb.value);
    document.getElementById('mesesHidden').value =
        selected.length > 0 ? selected.join(',') : '*';
}

function carregarCronExpression() {
    let cronExp = document.getElementById('originalCronExpression').value;
    if (!cronExp) cronExp = "0 0 * * * *";

    const partes = cronExp.split(' ');
    // Deve ter 6 partes (incluindo segundos)
    if (partes.length !== 6) return;

    const [, minutos, horas, diasMes, meses, diasSemana] = partes;

    // Configura minutos
    document.getElementById('minutosHidden').value = minutos;
    if (minutos !== '*') {
        let todosMultiplosDeCinco = true;
        minutos.split(',').forEach(min => {
            const checkbox = document.getElementById('min' + min.padStart(2, '0'));
            if (checkbox) checkbox.checked = true;
            if (min % 5 !== 0) todosMultiplosDeCinco = false;
        });
        document.getElementById('minutosEspecificos').checked = !todosMultiplosDeCinco;
        document.getElementById('minutosMultiplosDeCinco').checked = todosMultiplosDeCinco;
    }

    // Configura horas
    document.getElementById('horasHidden').value = horas;
    if (horas !== '*') {
        document.getElementById('horasEspecificas').checked = true;
        horas.split(',').forEach(hora => {
            const checkbox = document.getElementById('hora' + hora);
            if (checkbox) checkbox.checked = true;
        });
    }

    // Configura dias do mês
    document.getElementById('diasMesHidden').value = diasMes;
    if (diasMes !== '*') {
        document.getElementById('diasMesEspecificos').checked = true;
        diasMes.split(',').forEach(dia => {
            const checkbox = document.getElementById('dia' + dia);
            if (checkbox) checkbox.checked = true;
        });
    }

    // Configura meses
    document.getElementById('mesesHidden').value = meses;
    if (meses !== '*') {
        document.getElementById('mesesEspecificos').checked = true;
        meses.split(',').forEach(mes => {
            let mesId;
            switch (mes) {
                case '1':
                    mesId = 'janeiro';
                    break;
                case '2':
                    mesId = 'fevereiro';
                    break;
                case '3':
                    mesId = 'marco';
                    break;
                case '4':
                    mesId = 'abril';
                    break;
                case '5':
                    mesId = 'maio';
                    break;
                case '6':
                    mesId = 'junho';
                    break;
                case '7':
                    mesId = 'julho';
                    break;
                case '8':
                    mesId = 'agosto';
                    break;
                case '9':
                    mesId = 'setembro';
                    break;
                case '10':
                    mesId = 'outubro';
                    break;
                case '11':
                    mesId = 'novembro';
                    break;
                case '12':
                    mesId = 'dezembro';
                    break;
            }
            const checkbox = document.getElementById(mesId);
            if (checkbox) checkbox.checked = true;
        });
    }

    // Configura dias da semana
    document.getElementById('diasSemanaHidden').value = diasSemana;
    if (diasSemana !== '*') {
        document.getElementById('diasEspecificos').checked = true;
        diasSemana.split(',').forEach(dia => {
            let diaId;
            switch (dia) {
                case '0':
                    diaId = 'domingo';
                    break;
                case '1':
                    diaId = 'segunda';
                    break;
                case '2':
                    diaId = 'terca';
                    break;
                case '3':
                    diaId = 'quarta';
                    break;
                case '4':
                    diaId = 'quinta';
                    break;
                case '5':
                    diaId = 'sexta';
                    break;
                case '6':
                    diaId = 'sabado';
                    break;
            }
            const checkbox = document.getElementById(diaId);
            if (checkbox) checkbox.checked = true;
        });
    }

    toggleMinutos();
    toggleHoras();
    toggleDiasMes();
    toggleMeses();
    toggleDiasSemana();

}

function validarForm() {
    const erros = [];

    const nome = document.getElementById('nome').value;
    if (!nome || nome.trim() === '') {
        erros.push("O nome deve ser preenchido.");
    }

    const minutos = document.getElementById('minutosHidden').value;
    if (minutos === '*') {
        erros.push("Selecione pelo menos um minuto.");
    }
    return erros;
}

function montarCronExpression() {
    const minutos = document.getElementById('minutosHidden').value;
    const horas = document.getElementById('horasHidden').value;
    const diasMes = document.getElementById('diasMesHidden').value;
    const meses = document.getElementById('mesesHidden').value;
    const diasSemana = document.getElementById('diasSemanaHidden').value;

    document.getElementById('cronExpression').value =
        '0 ' + minutos + ' ' + horas + ' ' + diasMes + ' ' + meses + ' ' + diasSemana;
}

// executa a api/agendamento/{id}/ativo passando no body o novo valor da coluna ativo
function toogleAgendamentoAtivoCheckbox(checkboxElem) {
    fetch(`${URL.apiUrlBase}/agendamento/${checkboxElem.dataset.agendamentoId}/ativo`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ativo: checkboxElem.checked})
    })
        .then(response => {
            if (!response.ok) {
                checkboxElem.checked = !checkboxElem.checked;
                throw new Error('Falha ao atualizar o agendamento');
            }
            return response.json();
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao atualizar o status do agendamento');
        });
}

// obtem a data e hora do próximo evento do agendamento especificado pelo ID
function getProximoEvento(agendamentoId, offset, limit) {
    return fetch(`${URL.apiUrlBase}/agendamento/${agendamentoId}/next?offset=${offset}&limit=${limit}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Falha ao obter o próximo evento');
            }
            /*
            [
              {"agendamentoId": 1,
                "evento": "2025-06-04T10:00:00"}
            ]
             */
            return response.json();
        })
}

function carregarProximosEventos() {
    document.querySelectorAll('[id^="proximo-evento-"]').forEach(element => {
        const agendamentoId = element.id.replace('proximo-evento-', '');
        getProximoEvento(agendamentoId, 0, 1)
            .then(data => {
                if (data && data.length > 0) {
                    const evento = new Date(data[0].evento);
                    const formatada = evento.toLocaleString('pt-BR', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    });
                    return formatada;
                }
                return 'Não há eventos programados';
            })
            .then(data => {
                element.textContent = data;
            })
            .catch(error => {
                element.textContent = 'Erro ao carregar';
                console.error('Erro:', error);
            });
    });
}

function humanReadableCronExpression(cronExpression) {
    const parts = cronExpression.split(' ');
    if (parts.length !== 6) return cronExpression;

    const [segundo, minuto, hora, dia, mes, diaSemana] = parts;

    let texto = '';

    if (diaSemana !== '*') {
        const diasSemana = {
            '0': 'domingo', '1': 'segunda', '2': 'terça',
            '3': 'quarta', '4': 'quinta', '5': 'sexta', '6': 'sábado'
        };
        const dias = diaSemana.split(',')
            .map(d => diasSemana[d])
            .join(', ');
        texto += `Todo(s) ${dias}`;
    } else if (dia !== '*') {
        texto += `No dia ${dia}`;
        if (mes !== '*') {
            const meses = {
                '1': 'Janeiro', '2': 'Fevereiro', '3': 'Março',
                '4': 'Abril', '5': 'Maio', '6': 'Junho',
                '7': 'Julho', '8': 'Agosto', '9': 'Setembro',
                '10': 'Outubro', '11': 'Novembro', '12': 'Dezembro'
            };
            texto += ` de ${meses[mes]}`;
        }
    }

    if (hora !== '*') {
        texto += ` às ${hora}h`;
    }

    if (minuto !== '*') {
        if (texto === '') {
            texto = `Aos ${minuto} minutos`;
        } else {
            texto += `:${minuto}`;
        }
    }

    return texto || cronExpression;
}

