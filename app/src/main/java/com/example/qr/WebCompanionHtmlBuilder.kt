package com.example.qr

import com.example.data.CbcsCurriculumRepository
import com.example.data.CbcsHinglishContent
import com.example.data.CbcsTeluguContent
import com.example.data.CbcsWorkedProblems
import com.example.domain.model.AppLanguage

object WebCompanionHtmlBuilder {

    private fun jsonQuote(value: String): String {
        val sb = StringBuilder(value.length + 4)
        sb.append('"')
        for (ch in value) {
            when (ch) {
                '\\' -> sb.append("\\\\")
                '"' -> sb.append("\\\"")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                '<' -> sb.append("\\u003C")
                '>' -> sb.append("\\u003E")
                else -> sb.append(ch)
            }
        }
        sb.append('"')
        return sb.toString()
    }

    private fun problemsToJsonArray(topicId: String, lang: AppLanguage): String {
        val problems = CbcsWorkedProblems.getProblemsForTopic(topicId, lang)
        return problems.joinToString(prefix = "[", postfix = "]") { p ->
            val stepsJson = p.steps.joinToString(prefix = "[", postfix = "]") { s ->
                """{"title":${jsonQuote(s.title)},"expr":${jsonQuote(s.expression)},"exp":${jsonQuote(s.explanation)}}"""
            }
            """{"title":${jsonQuote(p.title)},"stmt":${jsonQuote(p.statement)},"steps":$stepsJson,"ans":${jsonQuote(p.finalAnswer)}}"""
        }
    }

    fun buildStandaloneHtml(initialLanguage: AppLanguage = AppLanguage.HINGLISH): String {
        val jsonPayload = CbcsCurriculumRepository.topics.joinToString(prefix = "[", postfix = "]") { topic ->
            val hi = CbcsHinglishContent.topics[topic.id]
            val te = CbcsTeluguContent.topics[topic.id]
            val formulasJson = topic.rtlOrFormulas.joinToString(prefix = "[", postfix = "]") { jsonQuote(it) }
            buildString {
                append('{')
                append("\"id\":").append(jsonQuote(topic.id)).append(',')
                append("\"unitId\":").append(jsonQuote(topic.unitId)).append(',')
                append("\"title\":").append(jsonQuote(topic.title)).append(',')
                append("\"weight\":").append(jsonQuote(topic.examWeightage)).append(',')
                append("\"sub_en\":").append(jsonQuote(topic.subtitle)).append(',')
                append("\"sub_hi\":").append(jsonQuote(hi?.subtitle ?: topic.subtitle)).append(',')
                append("\"sub_te\":").append(jsonQuote(te?.subtitle ?: topic.subtitle)).append(',')
                append("\"sum_en\":").append(jsonQuote(topic.summary)).append(',')
                append("\"sum_hi\":").append(jsonQuote(hi?.summary ?: topic.summary)).append(',')
                append("\"sum_te\":").append(jsonQuote(te?.summary ?: topic.summary)).append(',')
                append("\"formulas\":").append(formulasJson).append(',')
                append("\"prob_en\":").append(problemsToJsonArray(topic.id, AppLanguage.ENGLISH)).append(',')
                append("\"prob_hi\":").append(problemsToJsonArray(topic.id, AppLanguage.HINGLISH)).append(',')
                append("\"prob_te\":").append(problemsToJsonArray(topic.id, AppLanguage.TELUGU))
                append('}')
            }
        }

        return """<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Arch3D COA • Web Study Companion</title>
<style>
  :root { --bg:#070d1c; --card:#101a33; --border:#1e3058; --pri:#38bdf8; --ok:#10b981; --txt:#f1f5f9; --sub:#94a3b8; }
  * { box-sizing:border-box; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif; }
  body { margin:0; background:var(--bg); color:var(--txt); padding:16px; max-width:860px; margin:auto; }
  header { display:flex; flex-wrap:wrap; gap:10px; justify-content:space-between; align-items:center; margin-bottom:16px; }
  h1 { font-size:1.35rem; margin:0; color:var(--pri); }
  .pill-row { display:flex; gap:8px; flex-wrap:wrap; margin-bottom:12px; }
  button, select { background:var(--card); color:var(--txt); border:1px solid var(--border); padding:8px 12px; border-radius:10px; cursor:pointer; font-size:0.9rem; }
  button.active { background:var(--pri); color:#04121f; font-weight:700; border-color:var(--pri); }
  .card { background:var(--card); border:1px solid var(--border); border-radius:16px; padding:14px; margin-bottom:12px; }
  .badge { display:inline-block; background:#172b4d; color:var(--pri); font-size:0.75rem; padding:3px 8px; border-radius:999px; margin-bottom:6px; }
  .formula { font-family:ui-monospace,SFMono-Regular,Menlo,monospace; background:#091124; padding:8px 10px; border-radius:8px; color:#7dd3fc; font-size:0.85rem; margin:6px 0; overflow-x:auto; }
  .step { border-left:3px solid var(--pri); padding-left:10px; margin:10px 0; }
  .ans { background:rgba(16,185,129,0.14); border:1px solid var(--ok); color:#a7f3d0; padding:10px; border-radius:10px; font-weight:600; margin-top:8px; }
  .muted { color:var(--sub); font-size:0.9rem; }
</style>
</head>
<body>
<header>
  <div>
    <h1>Arch3D COA • Web Edition</h1>
    <div class="muted">CBCS Computer Architecture &amp; Digital Logic (Android &amp; Apple Browser)</div>
  </div>
  <select id="langSel" onchange="setLang(this.value)">
    <option value="en">English</option>
    <option value="hi">Hinglish</option>
    <option value="te">తెలుగు</option>
  </select>
</header>
<div id="unitBar" class="pill-row"></div>
<div id="content"></div>
<script>
const TOPICS = $jsonPayload;
const params = new URLSearchParams(window.location.search);
let lang = ['en','hi','te'].includes(params.get('lang')) ? params.get('lang') : '${initialLanguage.code}';
let activeTopicId = params.get('topic') || '';
let activeUnit = 'all';
document.getElementById('langSel').value = lang;

function setLang(l){ lang = l; render(); }
function selectTopic(id){ activeTopicId = id; render(); }

function render(){
  const units = ['all','unit0','unit1','unit2','unit3','unit4','unit5'];
  const unitLabels = {all:'All',unit0:'Digital Logic',unit1:'Unit I',unit2:'Unit II',unit3:'Unit III',unit4:'Unit IV',unit5:'Unit V'};
  document.getElementById('unitBar').innerHTML = units.map(u =>
    '<button class="'+(activeUnit===u?'active':'')+'" onclick="activeUnit=\''+u+'\';activeTopicId=\'\';render()">'+unitLabels[u]+'</button>'
  ).join('');

  const root = document.getElementById('content');
  if(activeTopicId){
    const t = TOPICS.find(x => x.id === activeTopicId) || TOPICS[0];
    const probs = t['prob_'+lang] || t.prob_en;
    root.innerHTML = '<button onclick="selectTopic(\'\')" style="margin-bottom:12px">← All Topics</button>' +
      '<div class="card"><span class="badge">'+t.weight+'</span><h2>'+t.title+'</h2>' +
      '<p>'+t['sum_'+lang]+'</p>' +
      '<h4>Formulas &amp; RTL</h4>' +
      t.formulas.map(f => '<div class="formula">'+f+'</div>').join('') + '</div>' +
      probs.map(p => '<div class="card"><span class="badge">Step-by-Step Solution</span><h3>'+p.title+'</h3><p class="muted">'+p.stmt+'</p>' +
        p.steps.map((s,i) => '<div class="step"><strong>Step '+(i+1)+': '+s.title+'</strong>' +
          (s.expr ? '<div class="formula">'+s.expr+'</div>' : '') +
          '<div>'+s.exp+'</div></div>').join('') +
        '<div class="ans">Answer: '+p.ans+'</div></div>'
      ).join('');
    return;
  }
  const list = activeUnit==='all' ? TOPICS : TOPICS.filter(x => x.unitId===activeUnit);
  root.innerHTML = list.map(t =>
    '<div class="card" onclick="selectTopic(\''+t.id+'\')" style="cursor:pointer">' +
    '<span class="badge">'+(unitLabels[t.unitId]||t.unitId)+' • '+t.weight+'</span>' +
    '<h3 style="margin:4px 0">'+t.title+'</h3>' +
    '<div class="muted">'+t['sub_'+lang]+'</div></div>'
  ).join('');
}
render();
</script>
</body>
</html>"""
    }
}
