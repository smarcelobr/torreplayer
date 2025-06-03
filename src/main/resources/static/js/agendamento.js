function toggleMinutos() {
    var checkboxes = document.getElementById('checkboxesMinutos');
    var hidden = document.getElementById('minutosHidden');
    if (document.getElementById('todosMinutos').checked) {
        checkboxes.classList.remove("agenda-checkboxes-show");
        checkboxes.classList.add("agenda-checkboxes-hidden");
        hidden.value = '*';
    } else {
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
        document.getElementById('minutosEspecificos').checked = true;
        minutos.split(',').forEach(min => {
            const checkbox = document.getElementById('min' + min.padStart(2, '0'));
            if (checkbox) checkbox.checked = true;
        });
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

// Executa quando a página carregar
document.addEventListener('DOMContentLoaded', carregarCronExpression);

function montarCronExpression() {
    const minutos = document.getElementById('minutosHidden').value;
    const horas = document.getElementById('horasHidden').value;
    const diasMes = document.getElementById('diasMesHidden').value;
    const meses = document.getElementById('mesesHidden').value;
    const diasSemana = document.getElementById('diasSemanaHidden').value;

    document.getElementById('cronExpression').value =
        '0 ' + minutos + ' ' + horas + ' ' + diasMes + ' ' + meses + ' ' + diasSemana;
}
