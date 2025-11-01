package com.ufersa.CodePublish.commons.domain.services;

import com.ufersa.CodePublish.commons.domain.entities.Category;
import com.ufersa.CodePublish.commons.domain.entities.ProgramingLanguage;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.commons.domain.repositories.CategoryRepository;
import com.ufersa.CodePublish.commons.domain.repositories.ProgramingLanguageRepository;
import com.ufersa.CodePublish.commons.domain.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class InitialMetadataPopulationService {

    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final ProgramingLanguageRepository programingLanguageRepository;

    public void persistInitialTags(){
        if(tagRepository.count()<1){
            String[] tagNames = {"java","python","csharp","cpp","javascript","typescript","php","go","rust","kotlin","swift","sql","bash",
                    "backend","frontend","fullstack","api","cli-tool","desktop","mobile","micro servicos","web","tarefas em lote",
                    "autenticacao","autorizacao","banco de dados","upload de arquivo","processamento de dados",
                    "geracao de relatorio","scheduler","fila de mensagens","rest-controller","ui",
                    "financas","saude","educacao","ecommerce","logistica","crm","erp","iot","analytics",
                    "exemplo","template","teste","prototipo","producao","deprecated","experimental","estavel",
                    "springboot","django","flask","react","angular","vue","express","node","quarkus","laravel",
                    "mvc","hexagonal","clean-architecture","event-driven","monolito","ddd","cqrs","repository-pattern"};
            List<Tag> tags = new ArrayList<>();
            for(String tagName : tagNames){
                if(tagRepository.findByName(tagName).isEmpty()) {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    tags.add(tag);
                }
            }

            tagRepository.saveAll(tags);
        }

    }

    public void persistInitialCategories(){
        if(categoryRepository.count()<1){
            String[] categoryNames = {"Desenvolvimento Web","Desenvolvimento Mobile","Desenvolvimento Desktop",
                    "Desenvolvimento de APIs","Aplicações Backend","Interfaces Frontend","Automação de Tarefas",
                    "Integração de Sistemas","Sistemas Embarcados","Engenharia de Dados","Ciência de Dados",
                    "Inteligência Artificial","Aprendizado de Máquina","Visão Computacional",
                    "Processamento de Linguagem Natural","Análise de Dados","Computação em Nuvem",
                    "DevOps e Infraestrutura","Segurança da Informação","Banco de Dados","Engenharia de Software",
                    "Arquitetura de Software","Padrões de Projeto","Testes e Qualidade de Software",
                    "Monitoramento e Observabilidade","Internet das Coisas","Computação Gráfica","Jogos Digitais",
                    "Sistemas Distribuídos","Ferramentas de Linha de Comando","Scripts e Utilitários",
                    "Modelos e Templates","Documentação Técnica"};
            List<Category> categories = new ArrayList<>();
            for(String categoryName : categoryNames){
                if(!categoryRepository.existsByName(categoryName)) {
                    Category category = new Category();
                    category.setName(categoryName);
                    categories.add(category);
                }
            }
            categoryRepository.saveAll(categories);
        }
    }

    public void persistInitialProgramingLanguages(){
        if(programingLanguageRepository.count()<1){
            String[] programingLanguageNames = {"JAVA","PYTHON","C","C++","C#","JAVASCRIPT","TYPESCRIPT",
                    "PHP","GO","RUST","KOTLIN","SWIFT","R","SQL","RUBY","PERL","DART","SCALA",
                    "HASKELL","LUA","OBJECTIVE-C","SHELL","BASH","POWERFUL","VBA","FORTRAN","COBOL",
                    "ELIXIR","ERLANG","JULIA","ASSEMBLY","F#","MATLAB","GROOVY","PLSQL","VHDL","VERILOG"};
            List<ProgramingLanguage> programingLanguages = new ArrayList<>();
            for(String programingLanguage : programingLanguageNames){
                if(!programingLanguageRepository.existsByName(programingLanguage)) {
                    ProgramingLanguage language = new ProgramingLanguage();
                    language.setName(programingLanguage);
                    programingLanguages.add(language);
                }
            }
            programingLanguageRepository.saveAll(programingLanguages);
        }
    }
}
